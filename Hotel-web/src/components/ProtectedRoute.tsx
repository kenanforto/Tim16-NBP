import { Navigate, useLocation } from 'react-router-dom';
import { useEffect } from 'react';
import { useUser } from '../context/UserContext';
import { getCurrentUser } from '../api/services/authService';

interface ProtectedRouteProps {
  children: React.ReactNode;
  allowedRoles?: string[];
}

export function ProtectedRoute({ children, allowedRoles }: ProtectedRouteProps) {
  const { user, setUser } = useUser();
  const location = useLocation();

  useEffect(() => {
    const checkUser = async () => {
      try {
        const response = await getCurrentUser();
        if (response && response.data) {
          setUser(response.data);
        } else {
          setUser(null);
        }
      } catch {
        setUser(null);
      }
    };
    if (!user) {
      checkUser();
    }
  }, [user, setUser]);

  if (user && allowedRoles && !allowedRoles.includes(user.role.name)) {
    // Redirect to home if user doesn't have required role
    return <Navigate to="/" replace />;
  }

  return <>{children}</>;
}
