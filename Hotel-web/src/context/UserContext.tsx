import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';
import type { User } from '../types/user';
import { useLocation, useNavigate } from 'react-router-dom';
import { getCurrentUser } from '../api/services/authService';
import constants from '../constants';

interface UserContextType {
  user: User | null;
  setUser: (user: User | null) => void;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

export const UserProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);
  const location = useLocation();
  const navigate = useNavigate();
  const publicPaths = constants.publicPaths;

  useEffect(() => {
    const handler = (event: MessageEvent) => {
      if (event.data && event.data.type === 'SET_USER') {
        setUser(event.data.user);
      }
      if (event.data && event.data.type === 'LOGOUT_USER') {
        setUser(null);
      }
    };
    window.addEventListener('message', handler);
    return () => window.removeEventListener('message', handler);
  }, []);

  useEffect(() => {
    const checkUser = async () => {
      console.log('Checking user authentication...', location.pathname, publicPaths, user);
      try {
        const response = await getCurrentUser();
        if (response && response.data) {
          setUser(response.data);
        } else {
          setUser(null);
          if (!publicPaths.includes(location.pathname)) {
            navigate('/login');
          }
        }
        console.log('User authentication check completed', response.data);
      } catch {
        setUser(null);
        if (!publicPaths.includes(location.pathname)) {
          navigate('/login');
        }
        console.error('Error checking user authentication', location.pathname);
      }
    };
    checkUser();
  }, [location.pathname, navigate, setUser]);

  return (
    <UserContext.Provider value={{ user, setUser }}>
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error('useUser must be used within a UserProvider');
  }
  return context;
};