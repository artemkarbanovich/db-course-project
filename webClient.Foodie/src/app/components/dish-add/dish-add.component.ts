import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { FileItem, FileUploader } from 'ng2-file-upload';
import { ToastrService } from 'ngx-toastr';
import { DishAdd } from 'src/app/models/dish-add';
import { DishService } from 'src/app/services/dish.service';

@Component({
  selector: 'app-dish-add',
  templateUrl: './dish-add.component.html',
  styleUrls: ['./dish-add.component.scss']
})
export class DishAddComponent implements OnInit {
  public dishForm: FormGroup;
  public uploader: FileUploader;

  constructor(private dishService: DishService, private router: Router,
    private formBuilder: FormBuilder, private toastr: ToastrService) { }

  public ngOnInit() : void {
    this.initializeForm();
    this.initializeUploader();
  }

  public addDish() : void {
    if(this.uploader.queue.length === 0 || this.dishForm.invalid) {
      return;
    }

    let cookingTime: string = this.dishForm.controls['cookingTime'].value;
    if(cookingTime.length == 5) cookingTime += ':00';

    let dish: DishAdd = {
      name: this.dishForm.controls['name'].value,
      cookingTime: cookingTime,
      dishWeight: Math.round(this.dishForm.controls['dishWeight'].value),
      ingredients: this.dishForm.controls['ingredients'].value,
      youWillNeed: this.dishForm.controls['youWillNeed'].value,
      price: this.dishForm.controls['price'].value,
      isVisible: this.dishForm.controls['isVisible'].value,
      photos: []
    };

    this.uploader.queue.forEach((img: FileItem) => dish.photos.push(img._file));

    this.dishService.addDish(dish).subscribe(() => {
      this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => this.router.navigate(['dishes']));
      this.toastr.success('Dish succesfully added');
    });
  }

  private initializeForm(): void {
    this.dishForm = this.formBuilder.group({
      name: ['', [Validators.required, 
        Validators.minLength(1), Validators.maxLength(25)]],
      cookingTime: ['', [Validators.required]],
      youWillNeed: ['', [Validators.required,
        Validators.minLength(3), Validators.maxLength(120)]],
      dishWeight: ['', [Validators.required,
        Validators.min(1), Validators.max(2000)]],
      ingredients: ['', [Validators.required,
        Validators.minLength(3), Validators.maxLength(120)]],
      price: ['', [Validators.required, Validators.min(1),
        Validators.max(500), Validators.pattern('^-?\\d*(\\.\\d+)?$')]],
      isVisible: [false, []]
    });
  }

  private initializeUploader(): void {
    this.uploader = new FileUploader({ 
      isHTML5: true,
      allowedFileType: ['image']
    });
  }
}
