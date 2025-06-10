import './App.css'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import NotFound from './pages/NotFound';
import Layout from './components/Layout';
import Rooms from './pages/Rooms';
import { SnackbarProvider } from 'notistack';
import { UserProvider } from './context/UserContext';
import { RoomProvider } from './context/RoomContext';
import { ImageProvider } from './context/ImageContext';
import Data from './pages/Data';
import { ProtectedRoute } from './components/ProtectedRoute';

function App() {
  return (
    <SnackbarProvider maxSnack={3} autoHideDuration={3000}>
      <Router>
        <UserProvider>
          <RoomProvider>
            <ImageProvider>
              <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                
                {/* Protected routes inside Layout */}
                <Route element={<Layout />}>
                  <Route path="/" element={
                    <ProtectedRoute>
                      <Home />
                    </ProtectedRoute>
                  } />
                  <Route path="/rooms" element={
                    <ProtectedRoute>
                      <Rooms />
                    </ProtectedRoute>
                  } />
                  <Route path="/data" element={
                    <ProtectedRoute allowedRoles={['ADMIN']}>
                      <Data />
                    </ProtectedRoute>
                  } />
                  <Route path="*" element={<NotFound />} />
                </Route>
              </Routes>
            </ImageProvider>
          </RoomProvider>
        </UserProvider>
      </Router>
    </SnackbarProvider>
  );
}

export default App;
