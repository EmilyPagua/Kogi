package com.example.kogimobile.kogi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    Chronometer cronometro;
    Button btInicio, btPausa, btReset, btVuelta;
    String estado = "inactivo";
    Long regCronometro ;
    String timelaps = "";
    TextView tvIntervalo;
    int lap = 0, d=0;
    LinearLayout crono_interf;
    String vuelta;
    long time;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cronometro = (Chronometer) findViewById(R.id.cronometro);
        crono_interf = (LinearLayout) findViewById(R.id.crono_interf);

        tvIntervalo = (TextView)findViewById(R.id.tvIntervalo);
        crono_interf.setBackgroundColor(Color.RED);
        btInicio = (Button)findViewById(R.id.btInicio);
        btPausa = (Button)findViewById(R.id.btPausa);
        btReset = (Button)findViewById(R.id.btReset);
        btVuelta = (Button)findViewById(R.id.btVuelta);

        btVuelta.setEnabled(false);
        btReset.setEnabled(false);
        btPausa.setEnabled(false);


        cronometro.setText("00:00:00.000");
        cronometro.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                time = SystemClock.elapsedRealtime() - chronometer.getBase();
                vuelta = convertirFormato(time);
                chronometer.setText(vuelta);

            }
        });

        btInicio.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (estado.equals("inactivo")) {
                    cronometro.setBase(SystemClock.elapsedRealtime());
                    cronometro.start();
                    estado = "activo";
                    btInicio.setEnabled(false);
                    btVuelta.setEnabled(true);
                    btReset.setEnabled(true);
                    btPausa.setEnabled(true);
                    lap =0;
                    crono_interf.setBackgroundColor(Color.YELLOW);
                    return;
                }
                if (estado.equals("pausado")) {
                    cronometro.setBase(cronometro.getBase() + SystemClock.elapsedRealtime() - regCronometro);
                    cronometro.start();
                    estado = "activo";
                    btInicio.setEnabled(false);
                    btPausa.setEnabled(true);
                    crono_interf.setBackgroundColor(Color.YELLOW);
                }
            }
        });

        btPausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    regCronometro = SystemClock.elapsedRealtime();
                    cronometro.stop();
                    estado = "pausado";
                    btInicio.setText("Resumir");
                    btInicio.setEnabled(true);
                    btPausa.setEnabled(false);
                    crono_interf.setBackgroundColor(Color.RED);
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cronometro.stop();
                estado = "inactivo";
                btInicio.setText("Iniciar");
                cronometro.setText("00:00:00:00.000");
                crono_interf.setBackgroundColor(Color.RED);
                tvIntervalo.setText(null);
                vuelta = null;
                timelaps = "";
                d=0;
                btVuelta.setEnabled(false);
                btReset.setEnabled(false);
                btPausa.setEnabled(false);
                btInicio.setEnabled(true);
            }
        });

        btVuelta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lap++;

                if (lap<=4){
                    timelaps = "Vuelta: " + "\tnº\t" + lap + ":\t "+vuelta+"  \n" + timelaps;
                    tvIntervalo.setText(timelaps);
                }else{
                    btVuelta.setEnabled(false);
                }

            }
        });
    }
     public String convertirFormato (long time){

         int h, m, s,ms;
         h = (int) (time / 3600000); //24
         m = (int) ((time - h * 3600000) / 60000); //0
         s = (int) (time - h * 3600000 - m * 60000) / 1000; //0
         ms = ((int) (time - h * 3600000 - m * 60000 - s * 1000));

         if ((h==23) && (m==59) && (s==59))
         {
            d++;
            cronometro.setBase(SystemClock.elapsedRealtime());
         }
         if (d == 1){
             cronometro.stop();
             estado = "inactivo";
             btInicio.setText("Iniciar");
             cronometro.setText("00:00:00:00.000");
             crono_interf.setBackgroundColor(Color.RED);
             tvIntervalo.setText(null);
             vuelta = null;
             timelaps = null;
             btVuelta.setEnabled(false);
             btReset.setEnabled(true);
             btPausa.setEnabled(false);
             btInicio.setEnabled(false);
         }

         String hh = h < 10 ? "0" + h : h + "";
         String mm = m < 10 ? "0" + m : m + "";
         String ss = s < 10 ? "0" + s : s + "";
         String mmss = s < 100 ? "" + ms : ms + "";
         String dd;

         dd = d < 10 ? "0" + d : d + "";
         return  dd + ":" + hh + ":" + mm + ":" + ss + "." + mmss;
     }

}
