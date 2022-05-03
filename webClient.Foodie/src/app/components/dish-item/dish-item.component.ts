import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { FileItem, FileUploader } from 'ng2-file-upload';
import { ToastrService } from 'ngx-toastr';
import { Dish } from 'src/app/models/dish';
import { DishUpdate } from 'src/app/models/dish-update';
import { Photo } from 'src/app/models/photo';
import { DishService } from 'src/app/services/dish.service';

@Component({
  selector: 'app-dish-item',
  templateUrl: './dish-item.component.html',
  styleUrls: ['./dish-item.component.scss']
})
export class DishItemComponent implements OnInit {
  public dish: Dish;
  public dishForm: FormGroup;
  public uploader: FileUploader;

  constructor(private dishService: DishService, private route: ActivatedRoute, 
    private toastr: ToastrService, private router: Router, private formBuilder: FormBuilder) { }
  
  public ngOnInit(): void {
    this.getDish();
    this.initializeUploader();
  }

  public updateGeneralInfo(): void {
    let cookingTime: string = this.dishForm.controls['cookingTime'].value;
    if(cookingTime.length == 5) cookingTime += ':00';

    const dish: DishUpdate = {
      id: this.dish.id,
      name: this.dishForm.controls['name'].value,
      cookingTime: cookingTime,
      dishWeight: Math.round(this.dishForm.controls['dishWeight'].value),
      ingredients: this.dishForm.controls['ingredients'].value,
      youWillNeed: this.dishForm.controls['youWillNeed'].value,
      price: this.dishForm.controls['price'].value,
      isVisible: this.dishForm.controls['isVisible'].value
    };

    this.dishService.updateDish(dish).subscribe(() => {
      this.dishForm.markAsPristine();
      this.toastr.success('Changes successfully saved ');
    })
  }

  public uploadPhotos(): void {
    let imageFiles: File[] = [];
    this.uploader.queue.forEach((img: FileItem) => imageFiles.push(img._file));

    this.dishService.uploadPhotos(imageFiles, this.dish.id).subscribe((images: Photo[]) => {
      this.uploader.queue = [];
      this.dish.photos = images;

      if(imageFiles.length === 1) {
        this.toastr.success('Photo succesfully added');
      } else {
        this.toastr.success('Photos succesfully added');
      }
    });
  }
  
  public deleteDishPhoto(photo: Photo): void {
    if(this.dish.photos.length === 1) {
      return;
    }

    this.dishService.deleteDishPhoto(photo.id).subscribe(() => {
      this.dish.photos.splice(this.dish.photos.indexOf(photo), 1)
      this.toastr.success('Photo succesfully deleted');
    });
  }

  private getDish(): void {
    this.route.params.subscribe((params: Params) => {
      this.dishService.getDish(params['id']).subscribe({
        next: (dish: Dish) => this.dish = dish,
        complete: () => this.initializeForm(),
        error: () => {
          this.toastr.error('Dish does not exist');
          this.router.navigate(['/dishes']);
        }
      });
    })
  }

  private initializeForm(): void {
    this.dishForm = this.formBuilder.group({
      name: [this.dish.name, [Validators.required, 
        Validators.minLength(1), Validators.maxLength(25)]],
      cookingTime: [this.dish.cookingTime, [Validators.required]],
      youWillNeed: [this.dish.youWillNeed, [Validators.required,
        Validators.minLength(3), Validators.maxLength(120)]],
      dishWeight: [this.dish.dishWeight, [Validators.required,
        Validators.min(1), Validators.max(2000)]],
      ingredients: [this.dish.ingredients, [Validators.required,
        Validators.minLength(3), Validators.maxLength(120)]],
      price: [this.dish.price, [Validators.required, Validators.min(1),
        Validators.max(500), Validators.pattern('^-?\\d*(\\.\\d+)?$')]],
      isVisible: [this.dish.isVisible, []]
    });
  }

  private initializeUploader(): void {
    this.uploader = new FileUploader({ 
      isHTML5: true,
      allowedFileType: ['image']
    });
  }
}
