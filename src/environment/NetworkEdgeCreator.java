package environment;

import repast.simphony.space.graph.EdgeCreator;

public class NetworkEdgeCreator<T> implements EdgeCreator<NetworkEdge<T>, T> {

	@Override
	public NetworkEdge<T> createEdge(T source, T target, boolean isDirected, double weight) {
		return new NetworkEdge<T>(source, target, isDirected, weight);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getEdgeType() {
		return NetworkEdge.class;
	}
}
