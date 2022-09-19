package irm

case class InformationGroup(
  number: Int,
  label: String)

case class System(
  number: Option[Int],
  label: String)

enum ConnectionType(val symbol: String):
  case Create extends ConnectionType("#x2B24")
  case Read extends ConnectionType("#x2B58")
  case Update extends ConnectionType("#x25C9")

case class Connection(
  system: System,
  informationGroup: InformationGroup,
  connectionType: ConnectionType)

object SampleData:

  val infEquipment = InformationGroup(1, "Equipment")
  val infShipment = InformationGroup(2, "Shipment")
  val infCustomerComplaint = InformationGroup(3, "Customer Complaint")
  val infProductPrice = InformationGroup(4, "Product Price")
  val infProduct = InformationGroup(5, "Product")
  val infOrder = InformationGroup(6, "Order")
  val infCustomerContract = InformationGroup(7, "Customer Contract")
  val infPartnerSettlement = InformationGroup(8, "Partner Settlement")
  val infDepot = InformationGroup(9, "Depot")
  val infServiceStation = InformationGroup(10, "Service Station")

  val informationGroups = List(
    infEquipment,
    infShipment,
    infCustomerComplaint,
    infProductPrice,
    infProduct,
    infOrder,
    infCustomerContract,
    infPartnerSettlement,
    infDepot,
    infServiceStation)

  val sysTruckPortalDenmark = System(Some(1), "Truck portal (Denmark)")
  val sysCO2Calculator = System(Some(2), "CO2Calculator")
  val sysCO2CalculatorSverige = System(Some(3), "CO2CalculatorSverige")
  val sysPlDcsB2B = System(Some(4), "PL DCS B2B")
  val sysPrisguiden = System(Some(5), "Prisguiden")
  val sysPlRadissOpsg = System(Some(6), "PL RADISS OPSG")
  val sysTankcom123Management = System(Some(7), "Taskcom (123 management)")
  val sysNetshopWebSapOil = System(Some(8), "Netshop web (SAP OIL)")
  val sysPals = System(Some(9), "PALS")

  val systems = List(
    sysTruckPortalDenmark,
    sysCO2Calculator,
    sysCO2CalculatorSverige,
    sysPlDcsB2B,
    sysPrisguiden,
    sysPlRadissOpsg,
    sysTankcom123Management,
    sysNetshopWebSapOil,
    sysPals)

  val connections = List(
    Connection(sysTruckPortalDenmark, infEquipment, ConnectionType.Create),
    Connection(sysCO2Calculator, infProduct, ConnectionType.Read),
    Connection(sysCO2CalculatorSverige, infProduct, ConnectionType.Read),
    Connection(sysPrisguiden, infProductPrice, ConnectionType.Read),
    Connection(sysPrisguiden, infProduct, ConnectionType.Read),
    Connection(sysPrisguiden, infCustomerContract, ConnectionType.Read),
    Connection(sysPlRadissOpsg, infShipment, ConnectionType.Create),
    Connection(sysPlRadissOpsg, infProductPrice, ConnectionType.Create),
    Connection(sysPlRadissOpsg, infProduct, ConnectionType.Create),
    Connection(sysPlRadissOpsg, infOrder, ConnectionType.Create),
    Connection(sysPlRadissOpsg, infDepot, ConnectionType.Create),
    Connection(sysPlRadissOpsg, infServiceStation, ConnectionType.Create),
    Connection(sysNetshopWebSapOil, infProductPrice, ConnectionType.Read),
    Connection(sysNetshopWebSapOil, infProduct, ConnectionType.Read),
    Connection(sysNetshopWebSapOil, infOrder, ConnectionType.Create),
    Connection(sysPals, infProductPrice, ConnectionType.Create),
    Connection(sysPals, infProduct, ConnectionType.Create),
    Connection(sysPals, infCustomerContract, ConnectionType.Create),
    Connection(sysPals, infPartnerSettlement, ConnectionType.Create),
    Connection(sysPals, infServiceStation, ConnectionType.Create),
  )

end SampleData
  