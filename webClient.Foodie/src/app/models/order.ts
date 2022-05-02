import { OrderDish } from "./order-dish";

export interface Order {
    id: number;
    orderDate: Date;
    deliveryDate: Date;
    totalPrice: number;
    status: string;
    address: string;

    email: string;
    firstName: string;
    lastName: string;
    phoneNumber: string;

    dishes: OrderDish[];
}
