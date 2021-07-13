package com.example.tornstocks.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tornstocks.Models.Stock;
import com.example.tornstocks.R;

import java.util.ArrayList;
import java.util.List;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockHolder> {
    private List<Stock> stocks = new ArrayList<>();
    private OnStockClickListener listener;

    @NonNull
    @Override
    public StockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_list_item, parent, false);
        return new StockHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockListAdapter.StockHolder holder, int position) {
        holder.price.setText(String.valueOf(stocks.get(position).getCurrent_price()));
        holder.acronym.setText(String.valueOf(stocks.get(position).getAcronym()));
        holder.name.setText(String.valueOf(stocks.get(position).getName()));
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
        notifyDataSetChanged();
    }

    public class StockHolder extends RecyclerView.ViewHolder {

        private TextView name, acronym, price;

        public StockHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.stock_list_item_name);
            acronym = itemView.findViewById(R.id.stock_list_item_acronym);
            price = itemView.findViewById(R.id.stock_list_item_price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    // No position means it may still be in delete animation and so position is undefined
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.OnStockClick(stocks.get(pos));
                    }
                }
            });
        }
    }

    public interface OnStockClickListener {
        void OnStockClick(Stock stock);
    }

    public void setOnStockClickListener(OnStockClickListener onStockClickListener) {
        this.listener = onStockClickListener;
    }
}
