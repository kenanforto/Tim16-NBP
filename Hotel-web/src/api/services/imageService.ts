import axios from '../axios';
import type { Image } from '../types';

export const getImagesByRoom = (roomId: number) => {
  return axios.get<Image[]>(`/api/image/room/${roomId}`);
};

export const uploadRoomImage = (file: File, roomId: number) => {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('roomId', roomId.toString());
  
  return axios.post<Image>('/api/image', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};