package irm

object Store:

  val informationGroups = SampleData.informationGroups.to(collection.mutable.Buffer)
  val systems = SampleData.systems.to(collection.mutable.Buffer)
  val connections = SampleData.connections.to(collection.mutable.Buffer)

  def clearInformationGroups() = 
    informationGroups.clear()
    clearConnections()

  def clearSystems() = 
    systems.clear()
    clearConnections()

  def clearConnections() = 
    connections.clear()

  def clear() =
    clearInformationGroups()
    clearSystems()

  def reset() =
    clear()
    informationGroups.appendAll(SampleData.informationGroups)
    systems.appendAll(SampleData.systems)
    connections.appendAll(SampleData.connections)