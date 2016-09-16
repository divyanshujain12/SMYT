package com.example.divyanshu.smyt.Models;

import com.neopixl.pixlui.components.edittext.EditText;

public class ValidationModel {
        public EditText editText;
        public int validationType;
        public String errorMessage;


        public ValidationModel(EditText editText, int validationType, String errorMessage) {
            this.editText = editText;
            this.validationType = validationType;
            this.errorMessage = errorMessage;
        }

        public EditText getEditText() {
            return editText;
        }

        public void setEditText(EditText editText) {
            this.editText = editText;
        }

        public int getValidationType() {
            return validationType;
        }

        public void setValidationType(int validationType) {
            this.validationType = validationType;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }