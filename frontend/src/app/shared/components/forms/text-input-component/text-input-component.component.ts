import {Component, Input} from '@angular/core';
import {ControlValueAccessor} from '@angular/forms';

/*
* Input text component with optional side icon
* */
@Component({
  selector: 'app-text-input-component',
  standalone: true,
  imports: [],
  template: `

  `
})
export class TextInputComponentComponent implements ControlValueAccessor{

  @Input() icon?: string;
  @Input() type: string = 'text';
  @Input() placeholder: string = '';
  @Input() hasError: boolean = false;

  value: string = '';
  isDisabled: boolean = false;

  private onChange: (val: string) => void = () => {};
  onTouched: () => void = () => {};

  onInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.value = input.value;
    this.onChange(this.value);
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
