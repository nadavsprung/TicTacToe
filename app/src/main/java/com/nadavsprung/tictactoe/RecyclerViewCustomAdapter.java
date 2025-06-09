package com.nadavsprung.tictactoe;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewCustomAdapter extends RecyclerView.Adapter<RecyclerViewCustomAdapter.ViewHolder> {

    private Log[] localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewId;
        private final TextView textViewWinner;
        private final TextView textViewMoves;
        private final TextView textViewDate;





        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textViewId = (TextView) view.findViewById(R.id.textViewid);
            textViewWinner = (TextView) view.findViewById(R.id.textViewWinner);
            textViewMoves = (TextView) view.findViewById(R.id.textViewMoves);
            textViewDate = (TextView) view.findViewById(R.id.textViewDate);


        }

//        public Layout getTextView() {
//            return textViewId;
//        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public RecyclerViewCustomAdapter(Log[] dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textViewId.setText(String.valueOf(localDataSet[position].getId()));
        viewHolder.textViewWinner.setText(localDataSet[position].getWinner());
        viewHolder.textViewMoves.setText(String.valueOf(localDataSet[position].getMoves()));
        viewHolder.textViewDate.setText(localDataSet[position].getDate());

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}