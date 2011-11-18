import com.ps.sr.model.Authority
import com.ps.sr.model.Person
import com.ps.sr.model.PersonAuthority
import com.ps.sr.model.Level
import com.ps.sr.model.Program
import com.ps.sr.model.LevelItem
import com.ps.sr.model.Subscription

class BootStrap {

    def bootStrapService

    def init = { servletContext ->

        //get some data loaded into the system.
        bootStrapService.initData();

    }
}
