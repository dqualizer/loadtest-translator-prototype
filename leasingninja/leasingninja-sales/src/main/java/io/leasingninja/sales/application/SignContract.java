package io.leasingninja.sales.application;

import io.hschwentner.dddbits.annotation.ApplicationService;
import io.leasingninja.riskmanagement.application.InboxApplicationService;
import io.leasingninja.sales.domain.*;

@ApplicationService
public class SignContract {

	private final Contracts contracts;
	private final InboxApplicationService riskmanagementInbox;

	public SignContract(Contracts contracts, InboxApplicationService riskmanagementInbox) {
		this.contracts = contracts;
		this.riskmanagementInbox = riskmanagementInbox;
	}

	public void with(ContractNumber number, SignDate signDate) {
		assert number != null;
		assert signDate != null;

		var contract = this.contracts.with(number);

        /**
         * @Eduard_Schander
         * Added this line to prevent error
         */
        contract.calculateInstallmentFor(LeaseTerm.ofMonths(48), Interest.of(3.7));

		contract.sign(signDate);

		this.contracts.save(contract);

		riskmanagementInbox.confirmSignedContract(number.value(), signDate.year(), signDate.month(), signDate.day());
	}

}
