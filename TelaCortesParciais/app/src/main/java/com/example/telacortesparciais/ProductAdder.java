package com.example.telacortesparciais;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdder extends LinearLayout {

    private int minValueAllowed = 1;
    private int maxValueAllowed = 999999;

    @BindView(R.id.minus)
    ImageButton minus;

    @BindView(R.id.plus)
    ImageButton plus;

    @BindView(R.id.value)
    EditText valueView;

    boolean isDisabled = false;

    private int value = 0;
    private OnValueChangeListener listener;
    private EmptyValueListener emptyListener;
    private ProductAdderWatcher watcher;

    public ProductAdder(Context context) {
        super(context);

        init();
    }

    public ProductAdder(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_product_adder, this, true);

        ButterKnife.bind(this);

        minus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDisabled && value > minValueAllowed) {
                    value--;
                    valueView.setText(String.valueOf(value));
                }
            }
        });

        plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDisabled && value < maxValueAllowed) {
                    value++;
                    valueView.setText(String.valueOf(value));
                }
            }
        });
        watcher = new ProductAdderWatcher();
        valueView.addTextChangedListener(watcher);

        valueView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && valueView.getText().toString().isEmpty()) {
                    if (emptyListener != null)
                        emptyListener.onEmptyValueSet(hasFocus);
                }
            }
        });

        valueView.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        setValue(value);
    }

    public void setNumberEditable(boolean editable) {
        valueView.setFocusable(editable);
        valueView.setFocusableInTouchMode(editable);
        valueView.setClickable(editable);
    }

    public void setMinValueAllowed(int value) {
        this.minValueAllowed = value;
    }

    public void setValue(int value) {
        this.value = value;
        watcher.reset();
        valueView.setText(String.valueOf(this.value));
    }

    public int getValue() {
        return this.value;
    }

    public void setOnValueChangeListener(OnValueChangeListener listener) {
        this.listener = listener;
    }

    public void setEmptyValueListener(EmptyValueListener emptyListener) {
        this.emptyListener = emptyListener;
    }

    public void disable() {
        isDisabled = true;
    }

    public void enable() {
        isDisabled = false;
    }

    public boolean isEmpty() {
        return valueView.getText().toString().isEmpty();
    }

    public void setMaxValueAllowed(int maxValueAllowed) {
        this.maxValueAllowed = maxValueAllowed;
    }

    public interface EmptyValueListener {
        void onEmptyValueSet(boolean hasFocus);
    }

    public interface OnValueChangeListener {
        void onValueChange(int value);
    }

    class ProductAdderWatcher implements TextWatcher {
        String current = null;
        String beforeChange;

        public void reset() {
            current = null;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeChange = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isDisabled && !s.toString().equals(current)) {
                current = beforeChange;
                valueView.setText(beforeChange);
            } else if (!s.toString().equals(current)) {
                String parsed;
                if (!s.toString().isEmpty())
                    parsed = String.valueOf(Integer.parseInt(s.toString()));
                else {
                    parsed = "";
                    current = parsed;
                    if (emptyListener != null)
                        emptyListener.onEmptyValueSet(true);
                    return;
                }

                if (parsed.equals(String.valueOf(minValueAllowed - 1))) {
                    if (current == null) {
                        current = String.valueOf(minValueAllowed - 1);
                        valueView.setText(String.valueOf(minValueAllowed - 1));
                        if (listener != null)
                            listener.onValueChange(value);
                    } else {
                        current = beforeChange;
                        valueView.setText(current);
                    }
                } else if (parsed.equals(String.valueOf(maxValueAllowed + 1))) {
                    if (current == null) {
                        current = String.valueOf(maxValueAllowed + 1);
                        valueView.setText(String.valueOf(maxValueAllowed + 1));
                        if (listener != null)
                            listener.onValueChange(value);
                    } else {
                        current = beforeChange;
                        valueView.setText(current);
                    }
                } else {
                    current = parsed;
                    value = Integer.valueOf(current);
                    if (listener != null)
                        listener.onValueChange(value);
                    valueView.setText(current);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            valueView.setSelection(valueView.getText().length());
        }
    }
}
