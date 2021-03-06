package ru.pavlik.chempred.client.services.prediction;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.pavlik.chempred.client.model.dao.CompoundDao;
import ru.pavlik.chempred.client.model.dao.NeuralNetworkParamDao;
import ru.pavlik.chempred.client.model.dao.StructureDao;
import ru.pavlik.chempred.client.model.dao.TrainMethod;

import java.util.List;

public interface PredictionServiceAsync {

    void predictLEL(String smiles, AsyncCallback<Double> async);

    void predictUEL(String smiles, AsyncCallback<Double> async);

    void predictUEL(StructureDao structure, AsyncCallback<Double> async);

    void predictLEL(StructureDao structure, AsyncCallback<Double> async);

    void predictAllCompounds(boolean useLEL, AsyncCallback<List<CompoundDao>> async);

    void loadNeuralNetworkParams(boolean useLEL, AsyncCallback<NeuralNetworkParamDao> async);

    void trainLELValue(List<CompoundDao> compounds, final TrainMethod trainMethod, AsyncCallback<Double> async);

    void trainUELValue(List<CompoundDao> compounds, final TrainMethod trainMethod, AsyncCallback<Double> async);
}
