import './SignIn.css';
import { Link } from 'react-router-dom';

function SignIn(){
	return(
		<> 
			<div className="logo-container">
				<img src="/images/favicon.ico" alt="NOG Logo" className="logo"></img>
			</div>
			<div className="container">
				<h1>Sign In</h1>
				<form className="login-form">
					<input id="UserName" placeholder="UserName" required/>
					<input type="password" id="password" name="password" placeholder="Password" required/>
					<button type="submit" >Sign In</button>
				</form>
				<p>New to NOG? <Link to="/signUp">Sign up now.</Link></p>
			</div>
	  </>
	);
}

export default SignIn;
