import { Route, Routes } from 'react-router-dom';
import './App.css';
import ResponsiveAppBar from './components/ResponsiveAppBar';
import About from './components/About'
import Documentation from './components/Documentation'
import Forum from './components/Forum'
import Addition from './components/Addition';
import Footer from './components/Footer'
import Edit from './components/Edit'


function App() {
  return (
    <div className="App">
      <ResponsiveAppBar />
      <Routes>
        <Route path="/" element={<About />} />
        <Route path="About" element={<About />} />
        <Route path="Documentation" element={<Documentation />} />
        <Route path="Forum" element={<Forum />} />
        <Route path="Addition" element={<Addition />} />
        <Route path="Edit" element={<Edit />} />
      </Routes>
      <Footer />
    </div>
  );
}

export default App;
