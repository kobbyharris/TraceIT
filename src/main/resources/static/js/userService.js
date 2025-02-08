import axios from 'axios';

// Backend API base URL
const API_BASE_URL = 'http://localhost:8080';

export const login = async (email, password) => {
    return await axios.post(`${API_BASE_URL}/api/auth/login`, { email, password });
};

export const fetchUserProfile = async () => {
    return await axios.get(`${API_BASE_URL}/api/users/profile`);
};

export const updateUserProfile = async (name, email) => {
    return await axios.put(`${API_BASE_URL}/api/users/profile`, { name, email });
};

export const changePassword = async (currentPassword, newPassword, confirmPassword) => {
    return await axios.put(`${API_BASE_URL}/api/users/password`, {
        currentPassword,
        newPassword,
        confirmPassword
    });
};
