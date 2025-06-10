import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';
import { getAllRooms } from '../api/services/roomService';
import { getRoomTypeById } from '../api/services/roomTypeService';
import type { Room } from '../types/room';

interface RoomContextType {
    rooms: Room[];
    setRooms: (rooms: Room[]) => void;
    refreshRooms: () => Promise<void>;
}

const RoomContext = createContext<RoomContextType | undefined>(undefined);

export const RoomProvider = ({ children }: { children: ReactNode }) => {
    const [rooms, setRooms] = useState<Room[]>([]);

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
        <RoomContext.Provider value={{ rooms, setRooms, refreshRooms }}>
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
