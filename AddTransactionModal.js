import React from 'react';
import PropTypes from 'prop-types';
import Modal from 'react-modal';
import {coldWalletService} from "../services/coldWalletService";
import {hotWalletService} from "../services/hotWalletService";
import {coinService} from "../services/coinService";
import {clientService} from "../services/clientService";
import {transactionService} from "../services/transactionService";

class TransAddModal extends React.Component {

    static propTypes = {
        modalIsOpen: PropTypes.bool.isRequired,
        closeModal: PropTypes.func.isRequired,
        clients: PropTypes.array.isRequired,
        clientId: PropTypes.number.isRequired,
        clientAccounts: PropTypes.array.isRequired,
        clientAccountId: PropTypes.number.isRequired,
        coldWallets: PropTypes.array.isRequired,
        coldWalletId: PropTypes.number.isRequired,
        hotWallets: PropTypes.array.isRequired,
        hotWalletId: PropTypes.number.isRequired,
        transaction: PropTypes.object.isRequired,
        coins: PropTypes.array.isRequired
           }

    constructor(props) {
        super(props);
        this.state = {
            modalIsOpen: false,
            clientId: -1,
            client: { clientId: props.clientId || -1, active: false },
            clientAccountId: -1,
            clientAccount: {clientAccountId: props.clientAccountId || -1, active: false},
            coldWalletId: -1,
            coldWallet: {coldWalletId: props.coldWalletId || -1, active: false },
            hotWalletId: -1,
            hotWallet: {hotWalletId: props.hotWalletId || -1, acitve: false },
            coinTypeId: -1,
            coin: {coinId: props.coinId || -1, active: false },
            transaction: []
        }
        this.handleClientChange = this.handleClientChange.bind(this);
        this.handleAccountChange = this.handleAccountChange.bind(this);
        this.handleColdWalletChange = this.handleColdWalletChange.bind(this);
        this.handleHotWalletChange = this.handleHotWalletChange.bind(this);
        this.handleCoinTypeChange = this.handleCoinTypeChange.bind(this);
    }

    handleClientChange(event) {
        const target = event.target;
        const value = target.value;
        const clientId = value;

        this.setState({
            clientId: clientId
        });
    }

    handleAccountChange(event) {
        const target = event.target;
        const value = target.value;
        const clientAccountId = value;

        this.setState({
           clientAccountId: clientAccountId
        });
    }

    handleColdWalletChange(event) {
        const target = event.target;
        const value = target.value;
        const coldWalletId = value;

        this.setState({
            coldWalletId: coldWalletId
        });
    }

    handleHotWalletChange(event){
        const target = event.target;
        const value = target.value;
        const hotWalletId = value;

        this.setState({
            hotWalletId: hotWalletId
        });
    }

    handleCoinTypeChange(event){
        const target = event.target;
        const value = target.value;
        const coinTypeId = value;

        this.setState({
            coinTypeId: coinTypeId
        });
    }

    componentWillReceiveProps = (nextProps) => {
        this.setState({
            modalIsOpen: nextProps.modalIsOpen,
            infoMessage: '',
            errorMessage: '',
            clients: nextProps.clients,
            clientId: nextProps.clientId,
            clientAccounts: nextProps.clientAccounts,
            clientAccountId: nextProps.clientAccountId,
            coldWallets: nextProps.coldWallets,
            coldWalletId: nextProps.coldWalletId,
            hotWallets: nextProps.hotWallets,
            hotWalletId: nextProps.hotWalletId,
            coins: nextProps.coins,
            coinTypeId: nextProps.coinTypeId
        }, () => {
            let clientId = this.state.clientId;
            let clientAccountId = this.state.clientAccountId;
            let coldWalletId = this.state.coldWalletId;
            let hotWalletId = this.state.hotWalletId;
            let coinTypeId = this.state.coinTypeId;
            let selectedClientId = { clientId: -1, active: false};
            let selectedClientAccountId = { clientAccountId: -1, active: false };
            let selectedcoldWalletId = {coldWalletId: -1, active: false };
            let selectedhotWalletId = {hotWalletId: -1, active: false };
            let selectedcoinTypeId = {coinTypeId: -1, active: false };

            if(clientId !== -1) {
                clientId = parseInt(clientId, 10);
                clientService.getClients(this.state.client)
                    .then((response) => {
                        selectedClientId = response.data;
                        this.setState({
                            infoMessage: '', errorMessage: '',
                            clientId: clientId,
                            client: Object.assign({}, selectedClientId)
                        });
                    })
            }
            else{
                this.setState({
                    infoMessage: '', errorMessage: '',
                    clientId: clientId,
                    client: Object.assign({}, selectedClientId)
                });
            }
            if(clientAccountId !== -1) {
                clientAccountId = parseInt(clientAccountId, 10);
                clientService.getClientAccounts(this.state.clientAccount)
                    .then((response) => {
                        selectedClientAccountId = response.data;
                        this.setState({
                            infoMessage: '', errorMessage: '',
                            clientAccountId: clientAccountId,
                            clientAccount: Object.assign({}, selectedClientAccountId)
                        });
                    })
            }
            else {
                this.setState({
                    infoMessage: '', errorMessage: '',
                    clientAccountId: clientAccountId,
                    client: Object.assign({}, selectedClientAccountId)
                });
            }
            if(coldWalletId !== -1){
                coldWalletId = parseInt(coldWalletId, 10);
                coldWalletService.getColdWallets(this.state.coldWallet)
                    .then((response) => {
                        selectedcoldWalletId = response.data;
                        this.setState({
                            infoMessage: '', errorMessage:'',
                            coldWalletId: coldWalletId,
                            coldWallet: Object.assign({}, selectedcoldWalletId)
                        });
                    })
            }
            else {
                this.setState({
                    infoMessage: '', errorMessage: '',
                    coldWalletId: coldWalletId,
                    coldWallet: Object.assign({}, selectedcoldWalletId)
                });
            }
            if(hotWalletId !== -1){
                hotWalletId = parseInt(hotWalletId, 10);
                hotWalletService.getHotWallets(this.state.hotWallet)
                    .then((response) =>{
                        selectedhotWalletId = response.data;
                        this.setState({
                            infoMessage: '', errorMessage: '',
                            hotWalletId: hotWalletId,
                            hotWallet: Object.assign({}, selectedhotWalletId)
                        });
                    })
            }
            else {
                this.setState({
                    infoMessage: '', errorMessage: '',
                    hotWalletId: hotWalletId,
                    hotWallet: Object.assign({}, selectedhotWalletId)
                });
            }
            if(coinTypeId !== -1) {
                coinTypeId = parseInt(coinTypeId, 10);
                coinService.getCoinTypes(this.state.coin)
                    .then((response) => {
                        selectedcoinTypeId = response.data;
                        this.setState({
                            infoMessage: '', errorMessage: '',
                            coinTypeId: coinTypeId,
                            coin: Object.assign({}, selectedcoinTypeId)
                        });
                    })
            }
            else {
                this.setState({
                    infoMessage: '', errorMessage: '',
                    coinTypeId: coinTypeId,
                    coin: Object.assign({}, selectedcoinTypeId)
                });
            }
            });
    }

  save = () => {
        const updateModel = {
            ColdWalletId: this.state.coldWallets.coldWalletId,
            HotWalletId: this.state.hotWallets.hotWalletId,
            Amount: this.state.transaction.Amount,
            Hash: this.state.transaction.Hash,
            Memo: this.state.transaction.Memo
        };
        if(this.state.transaction.transactionId !== -1) {
            transactionService.addTransaction(this.sate.transactionId, updateModel)
                .then((response) => {
                    this.props.closeModal();
                })
                .catch((error) => {
                    console.error(error.response.data);
                    if (error.response === 400)
                        this.setState({errorMessage: error.response.data});
                });
        } else {
                transactionService.addTransaction(updateModel)
                    .then((response) => {
                        this.props.closeModal();
                    })
                    .catch((error) => {
                        console.error(error.response.data);
                        if (error.response === 400)
                            this.setState({ errorMessage: error.response.data });
                    });
        }
  }

    closeModal = () => {
        this.props.closeModal();
    }

    render() {
        const customStyles = {
            displayNone: {
                'display': 'none'
            }, loaderStyle: {
                'width': '0px',
                'height': '22px'
            }, childStyle: {
                'display': 'block',
                'paddingLeft': '2px'
            }, marginLeft: {
                'marginLeft': '0 !important'
            },
            content: {
                position: 'absolute',
                top: '125px',
                left: '125px',
                right: '35%',
                minWidth: '350px',
                width: '750px',
                bottom: '45%',
                height: '750px',
                border: '1px solid #ccc',
                background: '#2B3E50',
                overflow: 'auto',
                WebkitOverflowScrolling: 'touch',
                borderRadius: '4px',
                outline: 'none',
                padding: '15px',
                color: 'white'
            },
            textWrapStyle: {
                'whiteSpace': 'pre-wrap'
            }
        };
        const tx = this.state.transaction;
        return (
            <Modal
                isOpen={this.state.modalIsOpen}
                shouldFocusAfterRender={true}
                shouldCloseOnOverlayClick={false}
                ariaHideApp={false}
                data={this.state.clients}
                noDataTest = "No Clients Found"
                onRequestClose={this.closeModal}
                style={customStyles}>
                <div className="h6">Add Transaction</div>
                <div className="container">
                    <form>
                        <div className="form-group-row">
                            <select className="form-control" id="clientList" name="clientList" onChange={this.handleClientChange} value={this.state.clientId || -1}>
                                <option key={-1} value={-1}>Select a Client</option>
                                {/*{this.state.client ? this.state.client.map(o => {
                                    return <option key={o.clientId} value={o.clientId}>(o.clientName)</option>
                                }) : <option>No Clients Selected</option> }*/}
                                {this.state.client.map(o => {
                                    return <option key={o.clientId} value={o.clientId}>(o.clientName)</option>
                                })}
                            </select>
                        </div>
                        <div className="form-group-row">
                            <select className="form-control" id="clientAccountList" name="clientAccountList" onChange={this.handleAccountChange} value={this.state.clientAccountId || -1}>
                                <option key={-1} value={-1}>Select a Client Account</option>
                                {this.state.clientAccounts ? this.state.clientAccounts.map(o => {
                                    return <option key={o.clientAccountId} value={o.clientAccountId}>(o.clientAccountName)</option>
                                }) : <option>No Client Accounts Selected</option>}
                            </select>
                        </div>
                        <div className="form-group-row">
                            <select className="form-control" id="coldWalletList" name="coldWalletList" onChange={this.handleColdWalletChange} value={this.state.coldWalletId || -1}>
                                <option key={-1} value={-1}>Select a Cold Wallet</option>
                                {this.state.coldWallets ? this.state.coldWallets.map(o => {
                                    return <option key={o.coldWalletId} value={o.coldWalletId}>(o.description)</option>
                                }) : <option>No Cold Wallets Selected</option>}
                            </select>
                        </div>
                        <div className="form-group-row">
                            <select className="form-control" id="hotWalletList" name="hotWalletList" onChange={this.handleHotWalletChange} value={this.state.hotWalletId || -1}>
                                <option key={-1} value={-1}>Select a Hot Wallet</option>
                                {this.state.hotWallets ? this.state.hotWallets.map(o => {
                                    return <option key={o.hotWalletId} value={o.hotWalletId}>(o.description)</option>
                                }) : <option>No Hot Wallets Selected</option>}
                            </select>
                        </div>
                        <div className="form-group-row">
                            <select className="form-control" id="coinTypeList" name="cointTypeList" onChange={this.handleCoinTypeChange} value={this.state.coinTypeId || -1}>
                                <option key={-1} value={-1}> Select a Coin Type</option>
                                {this.state.coins ? this.state.coins.map(o=> {
                                    return <option key={o.coinTypeId} value={o.coinTypeId}>(o.coinTypeName)</option>
                                }) : <option>No Coins Seelcted</option>}
                            </select>
                        </div>
                        <div className="form-group row">
                            <label htmlFor="type" className="col-sm-4 col-form-label">Type / Status</label>
                            <div className="col-sm-4">
                                <input className="form-control" type="text" id="type" readOnly value={tx.transactionTypeName}></input>
                            </div>
                            <div className="col-sm-4">
                                <input className="form-control" type="text" id="status" readOnly value={tx.transactionStatusName}></input>
                            </div>
                        </div>
                        <div className="form-group row">
                            <label htmlFor="ticker" className="col-sm-4 col-form-label">Ticker</label>
                            <div className="col-sm-8">
                                <input className="form-control" type="text" id="ticker" readOnly value={tx.ticker}></input>
                            </div>
                        </div>
                        <div className="form-group row">
                            <label htmlFor="gas" className="col-sm-4 col-form-label">Gas / GasLimit / Nonce</label>
                            <div className="col-sm-3">
                                <input className="form-control text-right" type="text" id="gas" readOnly value={tx.gas || ''}></input>
                            </div>
                            <div className="col-sm-3">
                                <input className="form-control text-right" type="text" id="gasLimit" readOnly value={tx.gasLimit || ''}></input>
                            </div>
                            <div className="col-sm-2">
                                <input className="form-control text-right" type="text" id="nonce" readOnly value={tx.nonce || ''}></input>
                            </div>
                        </div>
                        <div className="form-group row">
                            <label htmlFor="hash" className="col-sm-4 col-form-label">Hash</label>
                            <div className="col-sm-8">
                                <input className="form-control" type="text" id="hash" readOnly value={tx.hash || ''}></input>
                            </div>
                        </div>
                        <div className="form-group row">
                            <label htmlFor="memo" className="col-sm-4 col-form-label">Memo</label>
                            <div className="col-sm-8">
                                <input className="form-control" type="text" id="memo" readOnly value={tx.memo || ''}></input>
                            </div>
                        </div>
                        <div className="form-group row">
                            <label htmlFor="coinDescription" className="col-sm-4 col-form-label">Coin</label>
                            <div className="col-sm-8">
                                <input className="form-control" type="text" id="coinDescription" readOnly value={tx.coinDescription}></input>
                            </div>
                        </div>
                        <div className="form-group row">
                            <label htmlFor="formattedAmount" className="col-sm-4 col-form-label">Amount / Fee</label>
                            <div className="col-sm-4">
                                <input className="form-control text-right" type="text" id="formattedAmount" readOnly value={tx.formattedAmount}></input>
                            </div>
                            <div className="col-sm-4">
                                <input className="form-control text-right" type="text" id="formattedFee" readOnly value={tx.formattedFee}></input>
                            </div>
                        </div>
                    </form>
                </div>
                <button className="btn btn-success" onClick={() => this.save()}>Save</button>
                <button className="btn btn-danger float-right mt-3" onClick={() => this.closeModal()}>Cancel</button>
            </Modal>
        )
    }
}
export default TransAddModal
