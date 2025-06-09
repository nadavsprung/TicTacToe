package com.nadavsprung.tictactoe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewCustomAdapter extends RecyclerView.Adapter<RecyclerViewCustomAdapter.ViewHolder> {

    private Log[] localDataSet;

    // Holds references to the views in each row item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewId;
        private final TextView textViewWinner;
        private final TextView textViewMoves;
        private final TextView textViewDate;

        public ViewHolder(View view) {
            super(view);
            textViewId = view.findViewById(R.id.textViewid);
            textViewWinner = view.findViewById(R.id.textViewWinner);
            textViewMoves = view.findViewById(R.id.textViewMoves);
            textViewDate = view.findViewById(R.id.textViewDate);
        }
    }

    // Adapter constructor gets the dataset (array of logs)
    public RecyclerViewCustomAdapter(Log[] dataSet) {
        localDataSet = dataSet;
    }

    // Called when RecyclerView needs a new view (row)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row, viewGroup, false);
        return new ViewHolder(view);
    }

    // Called to display data at the specified position
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.textViewId.setText(String.valueOf(localDataSet[position].getId()));
        viewHolder.textViewWinner.setText(localDataSet[position].getWinner());
        viewHolder.textViewMoves.setText(String.valueOf(localDataSet[position].getMoves()));
        viewHolder.textViewDate.setText(localDataSet[position].getDate());
    }

    // Returns the number of items in the dataset
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
