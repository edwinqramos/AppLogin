package com.example.edwin.applogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class InicioActivity extends AppCompatActivity {

    RadioGroup rgroup1;
    RadioButton rbtnTelevisor, rbtnRadio, rbtnCocina;
    CheckBox chkDescuento10, chkDescuento20;
    EditText txtPrecioProducto, txtDescuento, txtTotalVenta;
    Button btnCalcularVenta;

    //Declarando variables
    int precio = 0;
    double dscto1=0, dscto2=0, total=0, dsctoTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        rgroup1 = (RadioGroup) findViewById(R.id.rgroup1);
        rbtnTelevisor = (RadioButton) findViewById(R.id.rbtnTelevisor);
        rbtnRadio = (RadioButton) findViewById(R.id.rbtnRadio);
        rbtnCocina = (RadioButton) findViewById(R.id.rbtnCocina);
        chkDescuento10 = (CheckBox) findViewById(R.id.chkDescuento10);
        chkDescuento20 = (CheckBox) findViewById(R.id.chkDescuento20);
        txtPrecioProducto = (EditText) findViewById(R.id.txtPrecioProducto);
        txtDescuento = (EditText) findViewById(R.id.txtDescuento);
        txtTotalVenta = (EditText) findViewById(R.id.txtTotalVenta);
        btnCalcularVenta = (Button) findViewById(R.id.btnCalcularVenta);

        rgroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbtnTelevisor:
                        precio = 1500;
                        break;
                    case R.id.rbtnRadio:
                        precio = 500;
                        break;
                    case R.id.rbtnCocina:
                        precio = 800;
                        break;
                }

                txtPrecioProducto.setText(String.valueOf(precio));
            }
        });

        chkDescuento10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkDescuento10.isChecked()) {
                    dscto1 = precio * 0.1;
                } else {
                    dscto1 = 0;
                }

                dsctoTotal = dscto1 + dscto2;
                txtDescuento.setText(String.valueOf(dsctoTotal));
            }
        });

        chkDescuento20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkDescuento20.isChecked()) {
                    dscto2 = precio * 0.2;
                } else {
                    dscto2 = 0;
                }

                dsctoTotal = dscto1 + dscto2;
                txtDescuento.setText(String.valueOf(dsctoTotal));
            }
        });

        btnCalcularVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalVenta = precio - dsctoTotal;
                txtTotalVenta.setText(String.valueOf(totalVenta));
            }
        });

    }
}





















