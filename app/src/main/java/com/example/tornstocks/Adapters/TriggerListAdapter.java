package com.example.tornstocks.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tornstocks.Models.Trigger;
import com.example.tornstocks.R;

import java.util.ArrayList;
import java.util.List;

public class TriggerListAdapter extends RecyclerView.Adapter<TriggerListAdapter.TriggerHolder>{

    private OnTriggerListener listener;
    private List<Trigger> triggers = new ArrayList<>();

    @NonNull
    @Override
    public TriggerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trigger_list_item, parent, false);
        return new TriggerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TriggerListAdapter.TriggerHolder holder, int position) {
        holder.trigger_price.setText(String.valueOf(triggers.get(position).getTrigger_price()));
        holder.acronym.setText(triggers.get(position).getAcronym());
        if (triggers.get(position).isIs_above()){
            holder.type.setText("Above");
        } else {
            holder.type.setText("Below");
        }
    }

    @Override
    public int getItemCount() {
        return triggers.size();
    }

    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
        notifyDataSetChanged();
    }

    public Trigger getTrigger(int index) { return triggers.get(index); }

    public class TriggerHolder extends RecyclerView.ViewHolder {

        private TextView acronym, trigger_price, type;

        public TriggerHolder(@NonNull View itemView) {
            super(itemView);
            acronym =  itemView.findViewById(R.id.trigger_list_item_acronym);
            trigger_price =  itemView.findViewById(R.id.trigger_list_item_trigger);
            type =  itemView.findViewById(R.id.trigger_list_item_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    // No position means it may still be in delete animation and so position is undefined
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.OnTriggerClick(triggers.get(pos));
                    }
                }
            });
        }
    }

    public interface OnTriggerListener{
        void OnTriggerClick(Trigger trigger);
    }

    public void setOnTriggerListener(OnTriggerListener onTriggerListener){
        this.listener = onTriggerListener;
    }
}
