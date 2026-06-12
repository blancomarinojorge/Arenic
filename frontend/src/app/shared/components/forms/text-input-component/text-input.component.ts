import {Component, inject, Input} from '@angular/core';
import {ControlValueAccessor, FormsModule, NgControl, ReactiveFormsModule} from '@angular/forms';
import {AppIconComponent, AppIconName} from '../../icons/app-icon/app-icon.component';
import {NgClass} from '@angular/common';

/*
* Input text component with optional side icon
* */
@Component({
  selector: 'app-text-input-component',
  standalone: true,
  imports: [
    AppIconComponent,
    FormsModule,
    ReactiveFormsModule,
    NgClass
  ],
  template: `
    <label for="{{id}}" [ngClass]="wrapperClasses">

      @if (icon) {
        <app-app-icon [icon]="icon" class="w-xl"/>
      }

      <input [type]="type"
             [name]="name || id"
             [id]="id"
             [placeholder]="placeholder"
             [disabled]="isDisabled"
             [value]="value"
             (input)="onInput($event)"
             (focus)="isFocused = true"
             (blur)="onBlur()"
             class="outline-none border-none bg-transparent w-full"
      />
    </label>
  `,
  styleUrl: 'text-input.component.css'
})
export class TextInputComponent implements ControlValueAccessor{
  //equivalent of doing formGroup.controls.email directly in the view,
  // ngControl represents the formControl object of this component (self:true)
  private ngControl = inject(NgControl, { optional: true, self: true });

  @Input() id?: string;
  @Input() name?: string;
  @Input() icon?: AppIconName;
  @Input() type: string = 'text';
  @Input() placeholder: string = '';
  /*Consumer can override error trigger logic*/
  @Input() errorState: (control: NgControl | null) => boolean = (control) =>
    !!(control?.invalid && control?.touched);
  /*And they can pass the error messages*/
  @Input() errorMessages: Record<string, string> = {};

  value: string = '';
  isDisabled: boolean = false;
  isFocused: boolean = false;

  /*estado de erro---------------------------------*/
  get hasError(): boolean {
    return this.errorState(this.ngControl);
  }

  get errorMessage(): string {
    const errors = this.ngControl?.errors;
    if (!errors) return '';

    //pick the first error found
    const errorKey = Object.keys(errors)[0];

    return this.errorMessages[errorKey] ?? 'Invalid';
  }

  get wrapperClasses(){
    return{
      'flex flex-row items-center gap-s text-content-secondary bg-bg-primary border px-m py-s rounded-2xs': true,

      /*state variants*/
      'border-border-primary': !this.hasError && !this.isFocused,
      'border-border-focus': !this.hasError && this.isFocused,
      'border-border-negative': this.hasError
    }
  }

  /*Boilerplate*/
  ngOnInit():void {
    if(this.ngControl){
      //wire this component as the value accessor
      this.ngControl.valueAccessor = this;
    }
  }

  private onChange: (val: string) => void = () => {};
  onTouched: () => void = () => {};

  onInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.value = input.value;
    this.onChange(this.value);
  }

  onBlur(){
    this.isFocused = false;
    this.onTouched();
  }

  // ControlValueAccessor interface
  writeValue(val: string): void {
    this.value = val ?? '';
  }

  registerOnChange(fn: (val: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.isDisabled = isDisabled;
  }
}
