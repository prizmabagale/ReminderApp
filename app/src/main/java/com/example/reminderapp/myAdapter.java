package com.example.reminderapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myAdapter  extends RecyclerView.Adapter<myAdapter.myviewholder>{
    ReminderActivity reminderActivity = new ReminderActivity();
    ArrayList<Model> dataholder = new ArrayList<Model>();
    Context context;
    public myAdapter(ArrayList<Model> dataholder, Context c) {
        context = c;
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_reminder_file, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.mTitle.setText(dataholder.get(position).getTitle());
        holder.mDate.setText(dataholder.get(position).getDate());
        holder.mTime.setText(dataholder.get(position).getTime());
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alertLayout = new AlertDialog.Builder(context);
                view = LayoutInflater.from(context).inflate(R.layout.edit_delete,null);
                alertLayout.setView(view);
                AlertDialog alert = alertLayout.create();
                alert.show();

                EditText et1 = view.findViewById(R.id.editTitle);
                et1.setText(String.valueOf(dataholder.get(holder.getAdapterPosition()).getTitle()));

                Button btnDel = view.findViewById(R.id.btnDelete);
                btnDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dbManager db = new dbManager(context);
                        db.onDelete(dataholder.get(holder.getAdapterPosition()).getId());
                        Toast.makeText(context,"Reminder Deleted successfully",Toast.LENGTH_SHORT).show();
                        alert.dismiss();
                        dataholder.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                });

                Button btnEdit = view.findViewById(R.id.btnEdit);
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dbManager db = new dbManager(context);
                        Model Model = new Model();
                        Model.setId(dataholder.get(holder.getAdapterPosition()).getId());
                        Model.setTitle(et1.getText().toString().trim() );
                        Model.setTime(dataholder.get(holder.getAdapterPosition()).getTime());
                        Model.setDate(dataholder.get(holder.getAdapterPosition()).getDate());
                        db.onUpdate(Model);

                        Toast.makeText(context,"Record Updated successfully", Toast.LENGTH_SHORT).show();
                        dataholder.set(holder.getAdapterPosition(),Model);
                        notifyDataSetChanged();
                        alert.dismiss();

                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class myviewholder extends RecyclerView.ViewHolder {

        TextView mTitle, mDate, mTime;

        View view;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            mDate = (TextView) itemView.findViewById(R.id.txtDate);
            mTime = (TextView) itemView.findViewById(R.id.txtTime);
            view = itemView;
        }
    }
}
