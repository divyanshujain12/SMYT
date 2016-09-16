package com.example.divyanshu.smyt.Utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.androidadvance.topsnackbar.TSnackbar;
import com.example.divyanshu.smyt.Interfaces.SnackBarCallback;
import com.example.divyanshu.smyt.Models.ValidationModel;
import com.example.divyanshu.smyt.R;
import com.neopixl.pixlui.components.edittext.EditText;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by divyanshu.jain on 9/16/2016.
 */
public class Validation {
    public final static int TYPE_NAME_VALIDATION = 0;
    public final static int TYPE_EMAIL_VALIDATION = 1;
    public final static int TYPE_PHONE_VALIDATION = 2;
    public final static int TYPE_PASSWORD_VALIDATION = 3;
    public final static int TYPE_EMPTY_FIELD_VALIDATION = 4;

    private ArrayList<ValidationModel> validationModels = new ArrayList<>();

    public void addValidationField(ValidationModel validationModel) {
        validationModels.add(validationModel);
    }

    public HashMap<EditText, String> validate(Activity activity) {
        TSnackbar snackbar = CommonFunctions.getInstance().getErrorSnackbar(activity);
        HashMap<EditText, String> valueMap = new HashMap<>();

        for (ValidationModel validationModel : validationModels) {
            String editTextValue = validationModel.editText.getText().toString();
            boolean status = false;
            switch (validationModel.validationType) {
                case TYPE_NAME_VALIDATION:
                    status = validateName(editTextValue);
                    break;
                case TYPE_EMAIL_VALIDATION:
                    status = validateEmail(editTextValue);
                    break;
                case TYPE_PHONE_VALIDATION:
                    status = validatePhone(editTextValue);
                    break;
                case TYPE_PASSWORD_VALIDATION:
                    status = validateName(editTextValue);
                    break;
                case TYPE_EMPTY_FIELD_VALIDATION:
                    status = validateName(editTextValue);
                    break;
            }
            if (!status) {
                snackbar.setText(validationModel.errorMessage).show();
                return null;

            }
            valueMap.put(validationModel.editText, editTextValue);
        }
        return valueMap;
    }

    private boolean validateName(String editTextValue) {
        if (editTextValue.length() == 0)
            return false;
        else
            return true;
    }

    private boolean validateEmail(String editTextValue) {
        if (editTextValue.length() == 0 || !Patterns.EMAIL_ADDRESS.matcher(editTextValue).matches())
            return false;
        else
            return true;
    }

    private boolean validatePhone(String editTextValue) {
        if (editTextValue.length() == 0 || !Patterns.PHONE.matcher(editTextValue).matches())
            return false;
        else
            return true;
    }


}
