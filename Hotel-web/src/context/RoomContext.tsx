import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';
import { getAllRooms } from '../api/services/roomService';
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


    useEffect(() => {
        refreshRooms();
    }, []);

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
