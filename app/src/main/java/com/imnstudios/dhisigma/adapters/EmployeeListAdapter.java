package com.imnstudios.dhisigma.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.imnstudios.dhisigma.R;
import com.imnstudios.dhisigma.models.EmployeeModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.Holder> {
    Context context;
    ArrayList<EmployeeModel> employeeArrayList;
    private DatabaseReference mDatabase;


    public EmployeeListAdapter(Context context, ArrayList<EmployeeModel> employeeArrayList) {
        this.context = context;
        this.employeeArrayList = employeeArrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        mDatabase = FirebaseDatabase.getInstance().getReference("Employees");
        return new EmployeeListAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final EmployeeListAdapter.Holder holder, int position) {
        holder.employeeCode.setText(String.valueOf(employeeArrayList.get(position).getEmployeeCode()));
        holder.employeeName.setText(String.valueOf(employeeArrayList.get(position).getEmployeeName()));

        final String key = String.valueOf(employeeArrayList.get(position).getEmployeeCode());
        if (employeeArrayList.get(position).getEmployeeImageUrl() != null) {
            Glide.with(context)
                    .load(employeeArrayList.get(position).getEmployeeImageUrl())
                    .into(holder.employeeImage);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("Sure?")
                        .setMessage("You want to delete?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabase.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Something's wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();
                    }
                }).show();


            }
        });

    }


    public int getItemCount() {
        return employeeArrayList.size();
    }



    public static class Holder extends RecyclerView.ViewHolder {
        private TextView employeeCode;
        private TextView employeeName;
        private ImageView employeeImage;
        private ImageView delete;
        private RelativeLayout container;

        public Holder(@NonNull View itemView) {
            super(itemView);
            this.employeeCode = itemView.findViewById(R.id.employee_code);
            this.employeeName = itemView.findViewById(R.id.employee_name);
            this.employeeImage = itemView.findViewById(R.id.employee_image);
            this.delete = itemView.findViewById(R.id.delete);
            this.container = itemView.findViewById(R.id.container);

        }
    }
}
