export interface NewDeviceDTO {
    description: string,
    address: string,
    hourlyConsumption: number,
    ownerId: string | null
}