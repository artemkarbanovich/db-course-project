import { Time } from "@angular/common";
import { Photo } from "./photo";

export interface Dish {
    id: number,
    name: string,
    cookingTime: Time,
    youWillNeed: string,
    dishWeight: number,
    price: number,
    isVisible: boolean,
    ingredients: string,
    photos: Photo[]
}
