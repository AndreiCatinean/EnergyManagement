'use client';

import {useState, useEffect} from "react";
import {Line} from "react-chartjs-2";
import './styles.css';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
} from "chart.js";
import Loading from "@/app/components/loading/Loading";
import {AppConstants} from "@/app/helper/AppConstants";
import {apiCall} from "@/app/api/ApiCall";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

interface EnergyConsumptionChartProps {
    deviceId: string;
    selectedDate: string;
}

interface ChartData {
    labels: string[];
    datasets: {
        label: string;
        data: number[];
        borderColor: string;
        backgroundColor: string[];
        borderWidth: number;
    }[];
}

const EnergyConsumptionChart = ({deviceId, selectedDate}: EnergyConsumptionChartProps) => {
    const [chartData, setChartData] = useState<ChartData | null>(null);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const fetchData = () => {
            setLoading(true);

            apiCall.get(AppConstants.MONITORING_URL + '/consumption/of-device/', {
                params: {
                    deviceId: deviceId,
                    day: selectedDate,
                },
            })
                .then((response) => {
                    const data: { maxHourlyConsumption: number; actualConsumption: number }[] = response.data;

                    const hours = Array.from({ length: 24 }, (_, index) => `${index}:00`);

                    const consumptionData = hours.map((hour, index) => {
                        const entry = data[index];
                        if (entry) {
                            return {
                                consumption: entry.actualConsumption,
                                isExceeding: entry.actualConsumption > entry.maxHourlyConsumption,
                            };
                        }
                        return { consumption: 0, isExceeding: false };
                    });

                    const consumptionValues = consumptionData.map(item => item.consumption);
                    const pointColors = consumptionData.map(item =>
                        item.isExceeding ? 'rgba(255, 99, 132, 1)' : 'rgba(75, 192, 192, 1)'
                    );

                    setChartData({
                        labels: hours,
                        datasets: [
                            {
                                label: "Energy Consumption (kWh)",
                                data: consumptionValues,
                                borderColor: "rgba(75, 192, 192, 1)",
                                backgroundColor: pointColors,
                                borderWidth: 1,
                            },
                        ],
                    });
                })
                .catch((err) => {
                    console.error("Error fetching data:", err);
                })
                .finally(() => {
                    setLoading(false);
                });
        };

        fetchData();
    }, [deviceId, selectedDate]);

    return (
        <div className="chartContainer">
            {loading ? (
                <Loading/>
            ) : chartData ? (
                <Line data={chartData} options={{responsive: true}}/>
            ) : (
                <p>No data available for the selected date.</p>
            )}
        </div>
    );
};

export default EnergyConsumptionChart;
