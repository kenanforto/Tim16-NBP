import { useEffect, useState } from 'react';
import { getCurrentUser } from '../api/services/authService';

function Home() {
  const [user, setUser] = useState<string | null>(null);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await getCurrentUser();
        setUser(response.data);
      } catch (error) {
        console.error('Failed to fetch user information:', error);
      }
    };

    fetchUser();
  }, []);

  return (
    <div>
      <h1>Welcome to the Home Page</h1>
      {user ? <p>User: {user}</p> : <p>Loading user information...</p>}
    </div>
  );
}

export default Home;
