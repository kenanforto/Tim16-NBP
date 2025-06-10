import { createContext, useContext, useState, type ReactNode } from 'react';
import { getAllRooms } from '../api/services/roomService';
import type { Room } from '../types/room';
import { getImagesByRoom } from '../api/services/imageService';

interface RoomContextType {
    rooms: Room[];
    setRooms: (rooms: Room[]) => void;
    refreshRooms: () => Promise<void>;
    setImageForRoom: (roomId: number, image: string) => void;
}

const RoomContext = createContext<RoomContextType | undefined>(undefined);

export const RoomProvider = ({ children }: { children: ReactNode }) => {
    const [rooms, setRooms] = useState<Room[]>([]);

    const setImageForRoom = (roomId: number, image: string) => {
        setRooms(prevRooms =>
            prevRooms.map(room =>
                room.id === roomId ? { ...room, image } : room
            )
        );
    }

    const refreshRooms = async () => {
        try {
            const response = await getAllRooms();
            console.log("Room API response", response);

            if (Array.isArray(response.data?.elements)) {
                // const enrichedRooms = await Promise.all(
    //     response.data.elements.map(async (room) => {
    //         try {
    //             const typeRes = await getRoomTypeById(room.roomTypeId);
    //             const typeDescription = typeRes.data?.description || '';
    //             return { ...room, typeDescription };
    //         } catch (err) {
    //             console.warn(`Failed to get room type for ID ${room.roomTypeId}`, err);
    //             return { ...room, typeDescription: '' };
    //         }
    //     })
    // );
                setRooms(response.data.elements);
                for (const room of response.data.elements) {
                    try {
                        const response = await getImagesByRoom(room.id);
                        if (response.data && response.data.length > 0) {
                            const firstImage = response.data[0];
                            setImageForRoom(room.id, firstImage.imageData);
                        }
                    } catch (error) {
                        console.error(`Failed to fetch images for room ${room.id}:`, error);
                    }
                }
            } else {
                console.warn("Expected elements array missing from response", response.data);
                setRooms([]);
            }
        } catch (error) {
            console.error('Failed to fetch rooms:', error);
            setRooms([]);
        }
    };

    return (
        <RoomContext.Provider value={{ rooms, setRooms, refreshRooms, setImageForRoom }}>
            {children}
        </RoomContext.Provider>
    );
};

export const useRooms = () => {
    const context = useContext(RoomContext);
    if (!context) {
        throw new Error('useRooms must be used within a RoomProvider');
    }
    return context;
};
