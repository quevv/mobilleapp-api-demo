package com.example.quevvse150165_lab10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quevvse150165_lab10.Model.Trainee;
import com.example.quevvse150165_lab10.Model.TraineeAdapter;
import com.example.quevvse150165_lab10.api.TraineeRepository;
import com.example.quevvse150165_lab10.api.TraineeService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TraineeService traineeService;
    ArrayList<Trainee> traineeArr;
    RecyclerView rvTrainee;
    TraineeAdapter adapter;
    ImageView updateBtn, deleteBtn;

    public enum DialogType {
        CREATE,
        UPDATE
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvTrainee = findViewById(R.id.rvTrainee);
        traineeService = TraineeRepository.getTraineeService();
        updateBtn = (ImageView) findViewById(R.id.updateBtn);
        deleteBtn = (ImageView) findViewById(R.id.deleteBtn);

        getTrainees();

    }

    private void getTrainees() {
        Call<Trainee[]> call = traineeService.getAllTrainees();
        call.enqueue(new Callback<Trainee[]>() {
            @Override
            public void onResponse(Call<Trainee[]> call, Response<Trainee[]> response) {
                traineeArr = new ArrayList<>();
                Trainee[] trainees = response.body();
                if (trainees == null) {
                    return;
                }
                for (Trainee trainee : trainees) {
                    traineeArr.add(trainee);
                }
                adapter = new TraineeAdapter( traineeArr, MainActivity.this );
                rvTrainee.setAdapter(adapter);
                rvTrainee.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }

            @Override
            public void onFailure(Call<Trainee[]> call, Throwable t) {
                Log.d("Error", "GET API Failed!");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_trainee, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuAddBtn) {
            DialogTrainee(DialogType.CREATE,null);
        }
        return super.onOptionsItemSelected(item);
    }

    public void DialogTrainee(DialogType dialogType, Trainee traineeToUpdate) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_trainee);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = 900; // custom width
        layoutParams.height = 1600; // custom height
        dialog.getWindow().setAttributes(layoutParams);

        TextView tvHeading = dialog.findViewById(R.id.tvHeading);
        EditText etName = dialog.findViewById(R.id.etName);
        EditText etEmail = dialog.findViewById(R.id.etEmail);
        EditText etPhone = dialog.findViewById(R.id.etPhone);
        RadioGroup rgGender = dialog.findViewById(R.id.rgGender);
        Button saveBtn = dialog.findViewById(R.id.saveBtn);
        Button exitBtn = dialog.findViewById(R.id.exitBtn);
        RadioButton rbMale = dialog.findViewById(R.id.rbMale);
        RadioButton rbFemale = dialog.findViewById(R.id.rbFemale);

        if (dialogType == DialogType.CREATE) {
            tvHeading.setText("CREATE NEW TRAINEE");
        } else {
            tvHeading.setText("UPDATE TRAINEE");
            etName.setText(traineeToUpdate.getName());
            etEmail.setText(traineeToUpdate.getEmail());
            etPhone.setText(traineeToUpdate.getPhone());
            if (traineeToUpdate.getGender().equals("Male")) {
                rbMale.setChecked(true);
            }else rbFemale.setChecked(true);
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedRadioButtonGenderId = rgGender.getCheckedRadioButtonId();
                RadioButton rbGetGender = dialog.findViewById(checkedRadioButtonGenderId);
                boolean isNotNull = isNotNullEditText(etName, etEmail, etPhone);
                if( !isNotNull ){
                    Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    Log.d("Status", "Null Detected!");
                }
                else{
                    String name = etName.getText().toString();
                    String email = etEmail.getText().toString();
                    String phone = etPhone.getText().toString();
                    String gender = rbGetGender.getText().toString();

                    Log.d("GENDER DATA", gender);

                    Trainee trainee = new Trainee(name, email, phone, gender);
                    if (dialogType == DialogType.CREATE) {
                        saveData(trainee);
                    } else {
                        updateTrainee(trainee.getId()+1, trainee);
                    }
                    dialog.dismiss();
                    getTrainees();
                }
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void updateTrainee(long traineeId, Trainee trainee) {
        Call<Trainee> call = traineeService.updateTrainees( traineeId, trainee );
        call.enqueue(new Callback<Trainee>() {
            @Override
            public void onResponse(Call<Trainee> call, Response<Trainee> response) {
                Toast.makeText(MainActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                Log.d("PUT API", "SUCCESSFULLY");
            }

            @Override
            public void onFailure(Call<Trainee> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Update Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isNotNullEditText(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (editText == null || editText.getText().toString().isEmpty()) {
                return false; // One of the EditTexts is empty or null
            }
        }
        return true; // All EditTexts are not null and have text
    }

    private void saveData(Trainee trainee) {

        try {
            Call<Trainee> call = traineeService.createTrainees(trainee);
            call.enqueue(new Callback<Trainee>() {
                @Override
                public void onResponse(Call<Trainee> call, Response<Trainee> response) {
                    if (response.body() != null) {
                        Toast.makeText(MainActivity.this, "Create Successfully", Toast.LENGTH_SHORT).show();
                        Log.d("POST API", "SUCCESSFULLY");
                    }
                }

                @Override
                public void onFailure(Call<Trainee> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Create Failed", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    public void deleteTrainee(long traineeId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Trainee");
        builder.setMessage("Are you sure you want to delete this Trainee?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Call<Trainee> call = traineeService.deleteTrainees(traineeId);
                call.enqueue(new Callback<Trainee>() {
                    @Override
                    public void onResponse(Call<Trainee> call, Response<Trainee> response) {
                        Toast.makeText(MainActivity.this, "Delete Successfully!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Trainee> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Delete Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
                getTrainees();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do something when "No" button is clicked
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}