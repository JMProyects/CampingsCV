package jacques.raul.uv.es;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CampingsAdapter extends RecyclerView.Adapter<CampingsAdapter.ViewHolder> {

    private final CampingsViewInterface cvi;

    private ArrayList<Camping> campings;
    private ArrayList<Camping> originalCampings;
    Context context;

    public CampingsAdapter(CampingsViewInterface cvi, ArrayList<Camping> campings, Context context) {
        this.cvi = cvi;
        this.context = context;
        this.campings = campings;
        this.originalCampings = new ArrayList<Camping>(campings);
    }

    @Override
    public int getItemCount() {
        return campings.size();
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public CampingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.camping_item, parent, false);
        return new ViewHolder(view, cvi);
    }

    @Override
    public void onBindViewHolder(@NonNull CampingsAdapter.ViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        final Camping camping = campings.get(position);
        holder.campingName.setText(camping.getNombre());
        holder.campingCategoria.setText(camping.getCategoria());
        holder.campingMunicipio.setText(camping.getMunicipio());
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView campingName, campingCategoria, campingMunicipio;

        public ViewHolder(@NonNull View itemView, CampingsViewInterface cvi) {
            super(itemView);

            campingName = itemView.findViewById(R.id.campingName);
            campingCategoria = itemView.findViewById(R.id.campingCategoria);
            campingMunicipio = itemView.findViewById(R.id.campingMunicipio);

            itemView.setOnClickListener(view -> {
                if(cvi != null){
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION){
                        cvi.onItemClick(pos);
                    }
                }
            });
        }
    }
    public void setFilteredList(ArrayList<Camping> filteredList) {
        //campings = new ArrayList<>(filteredList);
        campings.clear();
        campings.addAll(filteredList);
        notifyDataSetChanged();
    }
}
