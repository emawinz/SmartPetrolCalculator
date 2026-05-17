package com.example.smartpetrolcalculator;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private final double SUBSIDY_RATE = 1.99;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Spinner spinnerPetrolType = view.findViewById(R.id.spinnerPetrolType);
        EditText etPrice = view.findViewById(R.id.etPrice);
        EditText etFuelUsage = view.findViewById(R.id.etFuelUsage);
        Switch switchEligible = view.findViewById(R.id.switchEligible);
        TextView tvEligibleBadge = view.findViewById(R.id.tvEligibleBadge);
        Button btnCalculate = view.findViewById(R.id.btnCalculate);

        // Show/hide green badge
        switchEligible.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tvEligibleBadge.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        // Petrol type spinner
        String[] petrolTypes = {"RON95", "RON97", "Diesel"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                petrolTypes
        );
        spinnerPetrolType.setAdapter(adapter);

        // Calculate button
        btnCalculate.setOnClickListener(v -> {

            String petrolType = spinnerPetrolType.getSelectedItem().toString();
            String priceText = etPrice.getText().toString();
            String fuelText = etFuelUsage.getText().toString();

            if (priceText.isEmpty() || fuelText.isEmpty()) {
                Toast.makeText(requireContext(),
                        "Please fill all input fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            double pricePerLiter = Double.parseDouble(priceText);
            double fuelUsage = Double.parseDouble(fuelText);
            double totalPetrolCost = fuelUsage * pricePerLiter;

            double budiRebate = 0.00;
            if (petrolType.equals("RON95") && switchEligible.isChecked()) {
                budiRebate = fuelUsage * SUBSIDY_RATE;
            }

            double finalPayable = totalPetrolCost - budiRebate;

            // Show receipt dialog
            showReceiptDialog(petrolType, pricePerLiter, fuelUsage,
                    totalPetrolCost, budiRebate, finalPayable);
        });

        return view;
    }

    private void showReceiptDialog(String petrolType, double price,
                                   double fuel, double total,
                                   double rebate, double finalPayable) {

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_receipt);

        // Make dialog width full screen with margins
        dialog.getWindow().setLayout(
                (int) (getResources().getDisplayMetrics().widthPixels * 0.92),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Get current date and time
        String dateTime = new SimpleDateFormat(
                "dd MMM yyyy  •  hh:mm a", Locale.getDefault()
        ).format(new Date());

        // Fill in receipt fields
        ((TextView) dialog.findViewById(R.id.tvReceiptDate)).setText(dateTime);
        ((TextView) dialog.findViewById(R.id.tvPetrolType)).setText(petrolType);
        ((TextView) dialog.findViewById(R.id.tvPricePerLitre))
                .setText(String.format("RM %.2f / L", price));
        ((TextView) dialog.findViewById(R.id.tvFuelUsage))
                .setText(String.format("%.1f L", fuel));
        ((TextView) dialog.findViewById(R.id.tvTotalCost))
                .setText(String.format("RM %.2f", total));
        ((TextView) dialog.findViewById(R.id.tvBudiRebate))
                .setText(String.format("- RM %.2f", rebate));
        ((TextView) dialog.findViewById(R.id.tvFinalPayable))
                .setText(String.format("RM %.2f", finalPayable));

        // Close button
        dialog.findViewById(R.id.btnCloseReceipt).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}