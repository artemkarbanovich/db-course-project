import { Time } from "@angular/common";

export interface OrderDish {
    dishesCount: number;
    name: string;
    cookingTime: Time;
    dishWeight: number;
    price: number;
    ingredients: string;
}
