package irm

extension (x: Int) def blankZero = if x == 0 then "" else x.toString

def matrix(systems: List[System], informationGroups: List[InformationGroup], connections: List[Connection]) =
  <table class="matrix">
    <tr>
      <td colspan="2" rowspan="2" class="title">
        <div class="horizontal">Information group</div>
        <div class="vertical">System</div>
      </td>
      {
        informationGroups map { informationGroup =>
          <td class="label-aux horizontal">{ informationGroup.number }</td>          
        }
      }
      <td class="total-label-aux horizontal first">{ xml.EntityRef(ConnectionType.Create.symbol) }</td>
      <td class="total-label-aux horizontal">{ xml.EntityRef(ConnectionType.Read.symbol) }</td>
      <td class="total-label-aux horizontal">{ xml.EntityRef(ConnectionType.Update.symbol) }</td>
      <td></td>
    </tr>
    <tr>
      {
        informationGroups map { informationGroup =>
          <td class="label horizontal">{ informationGroup.label }</td>          
        }
      }
      <td class="total-label horizontal first">TOTAL NUMBER CREATE</td>
      <td class="total-label horizontal">TOTAL NUMBER READ</td>
      <td class="total-label horizontal">TOTAL NUMBER UPDATE</td>
      <td class="total-label horizontal sum-total">TOTAL</td>
    </tr>
    {
      systems map { system =>
        <tr>
          <td class="label-aux vertical">{ system.number.getOrElse("") }</td>   
          <td class="label vertical">{ system.label }</td>   
          {
            informationGroups map { informationGroup =>
              val connection = connections find { x => 
                (x.system == system) &&
                (x.informationGroup == informationGroup)
              }  
              connection.map(_.connectionType) match {
                case Some(ConnectionType.Create) => <td class="connection">{ xml.EntityRef(ConnectionType.Create.symbol) }</td>
                case Some(ConnectionType.Read) => <td class="connection">{ xml.EntityRef(ConnectionType.Read.symbol) }</td>
                case Some(ConnectionType.Update) => <td class="connection">{ xml.EntityRef(ConnectionType.Update.symbol) }</td>
                case None => <td class="connection"></td>
              }
            }
          }
          <td class="total horizontal first">{ connections.count(x => (x.system == system) && (x.connectionType == ConnectionType.Create)).blankZero }</td>
          <td class="total horizontal">{ connections.count(x => (x.system == system) && (x.connectionType == ConnectionType.Read)).blankZero }</td>
          <td class="total horizontal">{ connections.count(x => (x.system == system) && (x.connectionType == ConnectionType.Update)).blankZero }</td>
          <td class="total horizontal sum-total">{ connections.count(x => (x.system == system)).blankZero }</td>
        </tr>
      }
    }
    <tr>
      <td class="total-label-aux first vertical">{ xml.EntityRef(ConnectionType.Create.symbol) }</td>
      <td class="total-label first vertical">TOTAL NUMBER CREATE</td>
      {
        informationGroups map { informationGroup =>
          <td class="total vertical first">{ connections.count(x => (x.informationGroup == informationGroup) && (x.connectionType == ConnectionType.Create)).blankZero }</td>
        }
      }
      <td class="total">{ connections.count(x => (x.connectionType == ConnectionType.Create)).blankZero }</td>
      <td></td>
      <td></td>
      <td></td>
    </tr>          
    <tr>
      <td class="total-label-aux vertical">{ xml.EntityRef(ConnectionType.Read.symbol) }</td>
      <td class="total-label vertical">TOTAL NUMBER READ</td>
      {
        informationGroups map { informationGroup =>
          <td class="total vertical">{ connections.count(x => (x.informationGroup == informationGroup) && (x.connectionType == ConnectionType.Read)).blankZero }</td>
        }
      }      
      <td></td>
      <td class="total">{ connections.count(x => (x.connectionType == ConnectionType.Read)).blankZero }</td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td class="total-label-aux vertical">{ xml.EntityRef(ConnectionType.Update.symbol) }</td>
      <td class="total-label vertical">TOTAL NUMBER UPDATE</td>
      {
        informationGroups map { informationGroup =>
          <td class="total">{ connections.count(x => (x.informationGroup == informationGroup) && (x.connectionType == ConnectionType.Update)).blankZero }</td>
        }
      }      
      <td></td>
      <td></td>
      <td class="total">{ connections.count(x => (x.connectionType == ConnectionType.Update)).blankZero }</td>
      <td></td>
    </tr>
    <tr>
      <td></td>
      <td class="total-label vertical sum-total">TOTAL</td>
      {
        informationGroups map { informationGroup =>
          <td class="total sum-total">{ connections.count(x => (x.informationGroup == informationGroup)).blankZero }</td>
        }
      }      
      <td></td>
      <td></td>
      <td></td>
      <td class="total sum-total">{ connections.size.blankZero }</td>
    </tr>
  </table>
