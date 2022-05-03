import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Dish } from '../models/dish';
import { DishAdd } from '../models/dish-add';
import { DishList } from '../models/dish-list';
import { DishUpdate } from '../models/dish-update';
import { Photo } from '../models/photo';

@Injectable({
  providedIn: 'root'
})
export class DishService {
  private apiUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) { }
  
  public getDishList(nameSearchStr: string, orderBy: string, orderByType: string, isVisible: string): Observable<DishList[]> {
    let params = new HttpParams();

    if(nameSearchStr) params = params.append('nameSearchStr', nameSearchStr);
    if(orderBy) params = params.append('orderBy', orderBy);
    if(orderByType) params = params.append('orderByType', orderByType);
    if(isVisible) params = params.append('isVisible', isVisible);

    return this.http.get<DishList[]>(this.apiUrl + 'dishes/admin-list', { observe: 'response', params }).pipe(
      map((response: HttpResponse<DishList[]>) => {
        return response.body;
      })
    );
  }
  
  public getDish(id: number) : Observable<Dish> {
    return this.http.get<Dish>(this.apiUrl + 'dishes/' + id);
  }

  public updateDish(dish: DishUpdate) {
    return this.http.put(this.apiUrl + 'dishes/', dish);
  }
  
  public deleteDishPhoto(photoId: number): Observable<Object> {
    return this.http.delete(this.apiUrl + 'dishes/photos/' + photoId);
  }

  public uploadPhotos(photos: File[], dishId: number) : Observable<Photo[]> {
    let formData = new FormData();

    photos.forEach((img: File) => {
      formData.append('files', img);
    });
    
    return this.http.post<Photo[]>(this.apiUrl + 'dishes/' + dishId + '/photos', formData);
  }

  public addDish(dish: DishAdd) : Observable<Object> {
    let formData = new FormData();

    formData.append('name', dish.name);
    formData.append('cookingTime', dish.cookingTime);
    formData.append('dishWeight', dish.dishWeight.toString());
    formData.append('ingredients', dish.ingredients);
    formData.append('youWillNeed', dish.youWillNeed);
    formData.append('price', dish.price.toString());
    formData.append('isVisible', dish.isVisible.toString());

    dish.photos.forEach((img: File) => {
      formData.append('Photos', img);
    });
    
    return this.http.post(this.apiUrl + 'dishes/', formData);
  }
}
