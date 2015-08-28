package com.wsl.library.input.demo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.wsl.library.password.PwdDisplayView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int STATE_SETUP_FIRST_PASSWORD = 0;
    private static final int STATE_SETUP_SECOND_PASSWORD = 1;
    private static final int STATE_CONFIRM_PASSWORD = 2;

    private PwdDisplayView passwordView;
    private View inputParentView;
    private TextView titleView;

    private String[] firstPassword;
    private String[] secondPassword;

    private int state = STATE_SETUP_FIRST_PASSWORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        titleView = (TextView) findViewById(R.id.title);
        passwordView = (PwdDisplayView) findViewById(R.id.password);
        passwordView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showPasswordInput();
                return false;
            }
        });
        inputParentView = findViewById(R.id.inputParent);

        findViewById(R.id.toggleView).setOnClickListener(this);
        findViewById(R.id.input0).setOnClickListener(this);
        findViewById(R.id.input1).setOnClickListener(this);
        findViewById(R.id.input2).setOnClickListener(this);
        findViewById(R.id.input3).setOnClickListener(this);
        findViewById(R.id.input4).setOnClickListener(this);
        findViewById(R.id.input5).setOnClickListener(this);
        findViewById(R.id.input6).setOnClickListener(this);
        findViewById(R.id.input7).setOnClickListener(this);
        findViewById(R.id.input8).setOnClickListener(this);
        findViewById(R.id.input9).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);
    }

    private void updateTitle(int resId) {
        titleView.setText(resId);
    }

    private void updateState() {
        switch (state) {
            case STATE_SETUP_FIRST_PASSWORD:
                //notice user to input password again
                if(passwordView.hasFullPassword()) {
                    firstPassword = passwordView.getPassword();
                    passwordView.resetPassword();
                    updateTitle(R.string.setup_password_title_second);
                    state = STATE_SETUP_SECOND_PASSWORD;
                }
                break;
            case STATE_SETUP_SECOND_PASSWORD:
                if(firstPassword == null || firstPassword.length <= 0) {
                    //this must not happen
                    passwordView.resetPassword();
                    updateTitle(R.string.setup_password_title_first);
                    state = STATE_SETUP_FIRST_PASSWORD;
                    return;
                }
                if(passwordView.hasFullPassword()) {
                    secondPassword = passwordView.getPassword();
                    state = STATE_CONFIRM_PASSWORD;
                    updateState();
                }
                break;
            case STATE_CONFIRM_PASSWORD:
                if(firstPassword == null || firstPassword.length <= 0 ||
                        secondPassword == null || secondPassword.length <= 0 ||
                        (secondPassword.length != firstPassword.length)) {
                    //this must not happen
                    passwordView.resetPassword();
                    updateTitle(R.string.setup_password_title_first);
                    state = STATE_SETUP_FIRST_PASSWORD;
                    return;
                }
                boolean result = confirmPassword();
                Snackbar.make(passwordView, result ? "设置成功" : "设置失败", Snackbar.LENGTH_LONG).show();
                if(!result) {
                    Snackbar.make(passwordView, "两次密码不一致,请重新设置", Snackbar.LENGTH_LONG).show();
                    Arrays.fill(firstPassword, null);
                    Arrays.fill(secondPassword, null);
                    firstPassword = null;
                    secondPassword = null;
                    passwordView.resetPassword();
                    updateTitle(R.string.setup_password_title_first);
                    state = STATE_SETUP_FIRST_PASSWORD;
                } else {
                    Snackbar.make(passwordView, result ? "设置成功" : "设置失败", Snackbar.LENGTH_LONG).show();
                }
                break;
        }

    }

    private boolean confirmPassword() {
        int len = firstPassword.length;
        for(int i=0; i<len-1; i++) {
            if(!firstPassword[i].equals(secondPassword[i])) {
                return false;
            }
        }
        return true;
    }

    private void addPassword(String password) {
        passwordView.addPassword(password);
    }

    private void deletePassword() {
        passwordView.deletePassword();
    }

    private void showPasswordInput() {
        inputParentView.setVisibility(View.VISIBLE);
    }

    private void togglePasswordInput() {
        if (inputParentView.getVisibility() == View.VISIBLE) {
            inputParentView.setVisibility(View.GONE);
        } else {
            inputParentView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggleView:
                togglePasswordInput();
                break;
            case R.id.input0:
                addPassword("0");
                break;
            case R.id.input1:
                addPassword("1");
                break;
            case R.id.input2:
                addPassword("2");
                break;
            case R.id.input3:
                addPassword("3");
                break;
            case R.id.input4:
                addPassword("4");
                break;
            case R.id.input5:
                addPassword("5");
                break;
            case R.id.input6:
                addPassword("6");
                break;
            case R.id.input7:
                addPassword("7");
                break;
            case R.id.input8:
                addPassword("8");
                break;
            case R.id.input9:
                addPassword("9");
                break;
            case R.id.delete:
                deletePassword();
                break;
        }
        updateState();
    }

}
