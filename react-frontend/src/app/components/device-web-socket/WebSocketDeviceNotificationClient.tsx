'use client';
import {useEffect} from 'react';
import {Client} from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import {AppConstants} from "@/app/helper/AppConstants";

interface WebSocketDeviceNotificationClientProps {
    deviceId: string;
    onMessage: (message: string) => void;
}

const WebSocketDeviceNotificationClient = ({deviceId, onMessage}: WebSocketDeviceNotificationClientProps) => {

    useEffect(() => {
        const socket = new SockJS(AppConstants.MONITORING_URL + '/ws');

        const stompClient = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                stompClient.subscribe(`/topic/notifications/${deviceId}`, (messageOutput) => {
                    onMessage(messageOutput.body);
                });
            },
            onStompError: (frame) => {
                console.error('STOMP Error: ', frame);
            }
        });

        stompClient.activate();

        return () => {
            stompClient.deactivate().then(() => {
            }).catch((error) => {
                console.error('Error during STOMP disconnection:', error);
            });
        };
    }, [deviceId, onMessage]);
    return null;
};

export default WebSocketDeviceNotificationClient;
