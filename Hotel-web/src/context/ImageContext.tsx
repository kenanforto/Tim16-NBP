import { createContext, useContext, useState, type ReactNode } from 'react';
import { getImagesByRoom, uploadRoomImage } from '../api/services/imageService';
import type { Image } from '../types/image';

interface ImageContextType {
    imagesByRoom: Record<number, Image[]>;
    loadingRooms: Set<number>;
    getImagesForRoom: (roomId: number) => Promise<Image[]>;
    uploadImageForRoom: (file: File, roomId: number) => Promise<Image>;
    clearImagesForRoom: (roomId: number) => void;
}

const ImageContext = createContext<ImageContextType | undefined>(undefined);

export const ImageProvider = ({ children }: { children: ReactNode }) => {
    const [imagesByRoom, setImagesByRoom] = useState<Record<number, Image[]>>({});
    const [loadingRooms, setLoadingRooms] = useState<Set<number>>(new Set());

    const getImagesForRoom = async (roomId: number): Promise<Image[]> => {
        if (imagesByRoom[roomId]) {
            return imagesByRoom[roomId];
        }

        if (loadingRooms.has(roomId)) {
            return new Promise((resolve) => {
                const checkInterval = setInterval(() => {
                    if (!loadingRooms.has(roomId) && imagesByRoom[roomId] !== undefined) {
                        clearInterval(checkInterval);
                        resolve(imagesByRoom[roomId]);
                    }
                }, 100);
            });
        }

        try {
            setLoadingRooms(prev => new Set(prev).add(roomId));
            
            const response = await getImagesByRoom(roomId);
            const images = response.data || [];
            
            setImagesByRoom(prev => ({
                ...prev,
                [roomId]: images
            }));
            
            return images;
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        } catch (error: any) {
            console.error(`Failed to fetch images for room ${roomId}:`, error);
            
            if (error.response?.status === 404) {
                setImagesByRoom(prev => ({
                    ...prev,
                    [roomId]: []
                }));
                return [];
            } else {
                setImagesByRoom(prev => ({
                    ...prev,
                    [roomId]: []
                }));
                return [];
            }
        } finally {
            setLoadingRooms(prev => {
                const newSet = new Set(prev);
                newSet.delete(roomId);
                return newSet;
            });
        }
    };

    const uploadImageForRoom = async (file: File, roomId: number): Promise<Image> => {
        try {
            const response = await uploadRoomImage(file, roomId);
            const newImage = response.data;
            
            setImagesByRoom(prev => ({
                ...prev,
                [roomId]: [...(prev[roomId] || []), newImage]
            }));
            
            return newImage;
        } catch (error) {
            console.error(`Failed to upload image for room ${roomId}:`, error);
            throw error;
        }
    };

    const clearImagesForRoom = (roomId: number) => {
        setImagesByRoom(prev => {
            const newState = { ...prev };
            delete newState[roomId];
            return newState;
        });
    };

    return (
        <ImageContext.Provider value={{
            imagesByRoom,
            loadingRooms,
            getImagesForRoom,
            uploadImageForRoom,
            clearImagesForRoom
        }}>
            {children}
        </ImageContext.Provider>
    );
};

export const useImages = () => {
    const context = useContext(ImageContext);
    if (!context) {
        throw new Error('useImages must be used within an ImageProvider');
    }
    return context;
};