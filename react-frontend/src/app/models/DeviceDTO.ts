export interface DeviceDTO {
    id: string,
    description: string,
    address: string,
    hourlyConsumption: number,
    ownerId: string | null
}