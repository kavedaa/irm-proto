package irm

extension (x: Int) def blankZero = if x == 0 then "" else x.toString

def matrix(systems: List[System], informationGroups: List[InformationGroup], connections: List[Connection]) =
  <table class="matrix">
    <thead>
      <tr>
        <th></th>
        <th class="title"></th>
        {
          informationGroups map { informationGroup =>
            <th>{ informationGroup.number }</th>          
          }
        }
        <th>&#x26AB;</th>
        <th>&#x26AA;</th>
        <th></th>
        <th></th>
      </tr>
      <tr>
        <th></th>
        <th class="title">
        </th>
        {
          informationGroups map { informationGroup =>
            <th class="top">{ informationGroup.label }</th>          
          }
        }
        <th class="top">TOTAL NUMBER CREATE</th>
        <th class="top">TOTAL NUMBER READ</th>
        <th class="top">TOTAL NUMBER UPDATE</th>
        <th class="top total">TOTAL</th>
      </tr>
    </thead>
    <tbody>
      {
        systems map { system =>
          <tr>
            <td class="left">{ system.number.getOrElse("") }</td>   
            <td>{ system.label }</td>   
            {
              informationGroups map { informationGroup =>
                val dependency = connections find { x => 
                  (x.system == system) &&
                  (x.informationGroup == informationGroup)
                }  
                dependency.map(_.connectionType) match {
                  case Some(ConnectionType.Create) => <td class="symbol">&#x26AB;</td>
                  case Some(ConnectionType.Read) => <td class="symbol">&#x26AA;</td>
                  case Some(ConnectionType.Update) => <td class="symbol"></td>
                  case None => <td></td>
                }
              }
            }
            <td>{ connections.count(x => (x.system == system) && (x.connectionType == ConnectionType.Create)).blankZero }</td>
            <td>{ connections.count(x => (x.system == system) && (x.connectionType == ConnectionType.Read)).blankZero }</td>
            <td>{ connections.count(x => (x.system == system) && (x.connectionType == ConnectionType.Update)).blankZero }</td>
            <td class="total">{ connections.count(x => (x.system == system)).blankZero }</td>
          </tr>
        }
      }
      <tr>
        <td>&#x26AB;</td>
        <td>TOTAL NUMBER CREATE</td>
        {
          informationGroups map { informationGroup =>
            <td>{ connections.count(x => (x.informationGroup == informationGroup) && (x.connectionType == ConnectionType.Create)).blankZero }</td>
          }
        }
        <td>{ connections.count(x => (x.connectionType == ConnectionType.Create)).blankZero }</td>
        <td></td>
        <td></td>
        <td></td>
      </tr>          
      <tr>
        <td>&#x26AA;</td>
        <td>TOTAL NUMBER READ</td>
        {
          informationGroups map { informationGroup =>
            <td>{ connections.count(x => (x.informationGroup == informationGroup) && (x.connectionType == ConnectionType.Read)).blankZero }</td>
          }
        }      
        <td></td>
        <td>{ connections.count(x => (x.connectionType == ConnectionType.Read)).blankZero }</td>
        <td></td>
        <td></td>
      </tr>
      <tr>
        <td></td>
        <td>TOTAL NUMBER UPDATE</td>
        {
          informationGroups map { informationGroup =>
            <td>{ connections.count(x => (x.informationGroup == informationGroup) && (x.connectionType == ConnectionType.Update)).blankZero }</td>
          }
        }      
        <td></td>
        <td></td>
        <td>{ connections.count(x => (x.connectionType == ConnectionType.Update)).blankZero }</td>
        <td></td>
      </tr>
      <tr>
        <td></td>
        <td class="total">TOTAL</td>
        {
          informationGroups map { informationGroup =>
            <td class="total">{ connections.count(x => (x.informationGroup == informationGroup)).blankZero }</td>
          }
        }      
        <td></td>
        <td></td>
        <td></td>
        <td class="total">{ connections.size.blankZero }</td>
      </tr>
    </tbody>
  </table>
