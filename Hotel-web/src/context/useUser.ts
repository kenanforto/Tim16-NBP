import { useContext } from 'react';
import { UserContext } from './UserContext.tsx';

export const useUser = () => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error('useUser must be used within a UserProvider');
  }
  return context as {
    user: import('../types/user').User | null;
    setUser: (user: import('../types/user').User | null) => void;
  };
};
