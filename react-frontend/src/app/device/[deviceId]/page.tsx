'use client';

import React, {useEffect, useState} from "react";
import EnergyConsumptionChart from "@/app/components/energy-consumption-chart/EnergyConsumptionChart";
import Navbar from "@/app/components/navbar/Navbar";
import '../styles.css';
import {determineAuthorization} from "@/app/helper/PermissionHelper";
import {usePathname, useRouter} from "next/navigation";
import Loading from "@/app/components/loading/Loading";

const DevicePage = ({params}: { params: { deviceId: string } }) => {
    const {deviceId} = params;
    const router = useRouter()
    const pathname = usePathname()
    const [loading, setLoading] = useState<boolean>(true)
    const [selectedDate, setSelectedDate] = useState<string>(new Date().toISOString().split("T")[0]);

    const handleDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSelectedDate(e.target.value);
    };

    useEffect(() => {
        determineAuthorization(router, pathname)
            .then((finishedAuthCheck) => {
                if (finishedAuthCheck) {
                    setLoading(false);
                }
            })
            .catch((error) => {
                console.error("Error during authorization check:", error);
                setLoading(false);
            });
    }, [router, pathname]);




    return (
        <>
            <Navbar notLogin={true}/>
            {
                loading ? <Loading/> :
                    <div className="pageContainer">
                        <div className="contentContainer">
                            <h1 className="heading">Device Energy Consumption</h1>
                            <h2 className="subHeading">Device ID: {deviceId}</h2>

                            <input
                                type="date"
                                value={selectedDate}
                                onChange={handleDateChange}
                                max={new Date().toISOString().split("T")[0]}
                                className="dateInput"
                            />

                            <div className="chartContainer">
                                <EnergyConsumptionChart deviceId={deviceId} selectedDate={selectedDate}/>
                            </div>
                        </div>
                    </div>
            }
        </>
    );
};

export default DevicePage;
