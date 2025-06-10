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
function App() {

  return (
    <SnackbarProvider maxSnack={3} autoHideDuration={3000}>
      <Router>
        <UserProvider>
          <RoomProvider>
            <ImageProvider>
              <Routes>
                <Route element={<Layout />}>
                  <Route path="/" element={<Home />} />
                  <Route path="/rooms" element={<Rooms />} />
                  <Route path="*" element={<NotFound />} />
                </Route>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
              </Routes>
            </ImageProvider>
          </RoomProvider>
        </UserProvider>
      </Router>
    </SnackbarProvider>
  );
}

export default App;
