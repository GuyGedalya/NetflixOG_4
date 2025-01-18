import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePageUnregistered from '../home/HomePageUnregistered';
import SignIn from '../signIn/SignIn';
import './App.css';

function App() {
  return (
	<Router>
		<div className="App">
			<Routes>
				<Route path="/" element={<HomePageUnregistered/>} />
				<Route path="/signin" element={<SignIn/>} />
			</Routes> 
		</div>
	</Router>
  );
}

export default App;
