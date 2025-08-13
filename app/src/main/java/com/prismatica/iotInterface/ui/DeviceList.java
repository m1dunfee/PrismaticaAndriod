package com.prismatica.iotInterface.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
//import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prismatica.iotInterface.R;
import com.prismatica.iotInterface.data.Item;
import com.prismatica.iotInterface.data.ItemRepository;
import com.prismatica.iotInterface.util.DeviceAdapter;
import java.util.List;

public class DeviceList extends AppCompatActivity implements DeviceAdapter.Listener {
    private ItemRepository repo;
    private DeviceAdapter adapter;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_device_list);

        repo = new ItemRepository(this);

        RecyclerView rv = findViewById(R.id.device_list_recycler);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new DeviceAdapter(repo.readAll(), this);
        rv.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab_add_device);
        fab.setOnClickListener(v -> showCreateDialog());

//        FIXME: added the botton to the View and uncomment import
//        View btnSms = findViewById(R.id.btnSmsTest);
//        if (btnSms != null) {
//            btnSms.setOnClickListener(v -> Toast.makeText(this, "Open SmsNotificationsActivity", Toast.LENGTH_SHORT).show());
//            // startActivity(new Intent(this, SmsNotificationsActivity.class));
//        }
    }

    private void refresh() {
        List<Item> data = repo.readAll();
        adapter.setData(data);
    }

    private void showCreateDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_item, null);
        EditText etName = dialogView.findViewById(R.id.etItemName);
        EditText etQty = dialogView.findViewById(R.id.etItemQty);
        EditText etNotes = dialogView.findViewById(R.id.etItemNotes);

        new AlertDialog.Builder(this)
                .setTitle("Add Item")
                .setView(dialogView)
                .setPositiveButton("Save", (d, w) -> {
                    String name = etName.getText().toString().trim();
                    String qStr = etQty.getText().toString().trim();
                    String notes = etNotes.getText().toString().trim();
                    int qty = qStr.isEmpty() ? 0 : Integer.parseInt(qStr);
                    repo.create(name, qty, notes);
                    refresh();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override public void onEdit(Item it) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_item, null);
        EditText etName = dialogView.findViewById(R.id.etItemName);
        EditText etQty = dialogView.findViewById(R.id.etItemQty);
        EditText etNotes = dialogView.findViewById(R.id.etItemNotes);
        etName.setText(it.name);
        etQty.setText(String.valueOf(it.qty));
        etNotes.setText(it.notes);

        new AlertDialog.Builder(this)
                .setTitle("Edit Item")
                .setView(dialogView)
                .setPositiveButton("Update", (d, w) -> {
                    String name = etName.getText().toString().trim();
                    String qStr = etQty.getText().toString().trim();
                    String notes = etNotes.getText().toString().trim();
                    int qty = qStr.isEmpty() ? it.qty : Integer.parseInt(qStr);
                    repo.update(it.id, name, qty, notes);
                    refresh();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override public void onDelete(Item it) {
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Delete " + it.name + "?")
                .setPositiveButton("Delete", (d, w) -> { repo.delete(it.id); refresh(); })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
