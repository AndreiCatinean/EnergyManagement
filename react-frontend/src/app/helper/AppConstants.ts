export const AppConstants = {
    USER_URL: process.env.NEXT_PUBLIC_USER_BACKEND_URL,
    DEVICE_URL: process.env.NEXT_PUBLIC_DEVICE_BACKEND_URL,
    MONITORING_URL: process.env.NEXT_PUBLIC_ENERGY_BACKEND_URL,
    CHAT_URL: process.env.NEXT_PUBLIC_CHAT_BACKEND_URL,
    CONVERSATION: "/conversation",
    CONSUMPTION: "/consumption/of-device",
    USER: '/user',
    LOGIN: '/login',
    DEVICE: '/device'
} as const; 