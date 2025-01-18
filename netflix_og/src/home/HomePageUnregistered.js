import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import './HomePageUnregistered.css';

function HomePageUnregistered(){
	const navigate = useNavigate();
	return(
		<div>
			<div className="top-bar">
				<img src="/images/favicon.ico" alt="NOG Logo" className="logo"></img>
				<Link to="/signin" className="sign-in-button">Sign In</Link>
			</div>
				<div className="container">
				<h1>WELCOME TO NOG<br/>Unlimited movies,<br/>TV shows, and more</h1>
				<p>Ready to watch?</p>
				<button onClick={() => navigate('/signup')} className="submit-button">Get Started &gt;</button>
			</div>
		</div>
	);
}


export default HomePageUnregistered;