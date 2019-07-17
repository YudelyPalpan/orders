<template>
  <div>
    <v-layout row wrap justify-center align-center>
      <v-flex xs8>
        <h1><v-icon large class="mr-2">shopping_cart</v-icon>Nueva orden de compra</h1>
      </v-flex>
      <v-flex class="text-sm-right" xs4>
        <v-btn @click="registerOrder" 
          :loading="busy"
          color="primary" 
          round
          outline 
          dark 
          >
          <v-icon class="mr-2">add_circle_outline</v-icon>
          Registrar orden
        </v-btn>
      </v-flex>
    </v-layout>
    <v-divider class="mb-4"></v-divider>
    <v-layout row wrap justify-center align-center>
      <v-flex xs12 mb-3>
        <h3><v-icon class="mr-2">person</v-icon>Cliente</h3>
      </v-flex>
      <v-flex xs9 sm9 md9 pr-3>
        <v-select
          v-model="customerId"
          :items="customers"
          item-text="fullName"
          item-value="id"
          box
          label="Clientes"></v-select>
      </v-flex>
      <v-flex xs3 sm3 md3>
        <v-text-field
          v-model="orderNumber"
          label="Número de orden"
          mask="######"
          box
        ></v-text-field>
      </v-flex>
    </v-layout>
    <v-layout row wrap justify-center align-center>
      <v-flex xs12 mb-3>
        <h3><v-icon class="mr-2">shopping_basket</v-icon>Productos</h3>
      </v-flex>
      <v-flex xs8 sm8 md8 pr-3>
        <v-select
          v-model="product"
          :items="products"
          item-text="productName"
          item-value="id"
          box
          return-object
          label="Productos"
        ></v-select>
      </v-flex>
      <v-flex xs3 sm3 md3>
        <v-text-field
          v-model="quantity"
          label="Cantidad"
          mask="###"
          box
        ></v-text-field>
      </v-flex>
      <v-flex xs1 sm1 md1 class="text-sm-right">
        <v-btn color="primary" outline dark fab @click="addProductToOrder" bottom>
          <v-icon>add_shopping_cart</v-icon>
        </v-btn>
      </v-flex>
    </v-layout>
    <v-layout row wrap align-center justify-center>
      <v-flex>
        <v-data-table
          :headers="headers"
          :items="productsTable"
          class="elevation-1"
        >
          <template v-slot:items="props">
            <td>{{ props.item.id }}</td>
            <td class="text-xs-right">{{ props.item.productName }}</td>
            <td class="text-xs-right">{{ props.item.stock }}</td>
            <td class="text-xs-right">${{ props.item.unitPrice }}</td>
            <td class="text-xs-right">{{ props.item.quantity }}</td>
            <td class="text-xs-right">${{ props.item.quantity *  props.item.unitPrice }}</td>
          </template>
        </v-data-table>
      </v-flex>
    </v-layout>
  </div>
</template>

<script>
import Logo from '~/components/Logo.vue'
import VuetifyLogo from '~/components/VuetifyLogo.vue'

export default {
  loading: false,
  components: {
    Logo,
    VuetifyLogo
  },
  data(){
    return {
      busy: false,
      headers: [
        {
          text: 'Id',
          align: 'left',
          sortable: false,
          value: 'id'
        },
        { text: 'Nombre del Producto', value: 'productName' },
        { text: 'Stock', value: 'stock' },
        { text: 'Precio Unitario', value: 'unitPrice' },
        { text: 'Cantidad', value: 'quantity' },
        { text: 'Total', value: 'quantity' }
      ],
      customerId: 1,
      orderNumber: "0000000",
      productsTable: [],
      product: {},
      quantity: 1
    }
  },
  async asyncData({ $axios }) {
    const products = await $axios.$get('/api/products');
    const customers = await $axios.$get('/api/customers');
    return {
      products,
      customers: customers.map(c => ({...c, fullName: `${c.firstName} ${c.lastName}`}))
    }
  },
  methods: {
    addProductToOrder(){
      const { product, quantity } = this;
      const detail = {
        id: product.id,
        productName: product.productName,
        stock: product.stock,
        unitPrice: product.unitPrice,
        quantity: parseInt(quantity),
      };

      
      this.productsTable.push(detail);
      this.product = {};
      this.quantity = 1;
    },
    async registerOrder(){
      const { customerId, orderNumber, productsTable } = this;
      if(productsTable.length !== 0) {
        this.busy = true;
        
        const order = await this.$axios.$post('/api/orders', {
          orderNumber,
          customerId,
          orderItem: productsTable.map(row => ({ 
            productId: row.id,
            unitPrice: row.unitPrice,
            quantity: row.quantity
          }))
        });
        this.busy = false;
        
        this.resetOrder();
    
        this.showOrderSuccessDialog(order);
  
      } else {
        await this.$dialog.warning({
          title: 'Orden inválida',
          text: 'No se agregó ningún producto a la orden'
        });
      }
    },
    showOrderSuccessDialog({ id }) {
      return this.$dialog.message.success(
        `La order '${id}' está siendo procesada, el cliente recibirá un correo dentro de poco.`, {
        position: 'top-center'
      });
    },
    async resetOrder() {
      this.customerId = 1,
      this.orderNumber = "0000000",
      this.productsTable = [],
      this.product = {},
      this.quantity = 1
    }
  }
}
</script>
