'use client';
import {FormEvent, useEffect, useState} from 'react';
import {loginUser} from "@/app/login/api";
import {redirectBasedOnUser} from "@/app/helper/PermissionHelper";
import {useRouter} from "next/navigation";
import './styles.css';
import Navbar from "@/app/components/navbar/Navbar";

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const router = useRouter();


    useEffect(() => {
        redirectBasedOnUser(router).then(() => {});
    }, []);

    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();
        loginUser({username, password})
            .then(response => {
                sessionStorage.setItem('loggedUser', JSON.stringify(response.data));
                redirectBasedOnUser(router).then(() =>{} );
            })
            .catch(() => alert("Wrong credentials"));
    };

    return (
        <>
            <Navbar notLogin={false}></Navbar>
            <div className="login-container">
                <h2>Login</h2>
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="username">Username:</label>
                        <input
                            type="username"
                            id="username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Password:</label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit">Login</button>
                </form>
            </div>
        </>
    );
};

export default Login;
