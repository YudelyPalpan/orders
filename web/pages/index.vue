<template>
  <div>
    <v-layout
      row
      wrap
      justify-center
      align-center>
      <v-flex
        xs9
        sm9
        md9>
        <v-select
          v-model="customerId"
          :items="customers"
          item-text="fullName"
          item-value="id"
          box
          label="Clientes"></v-select>
      </v-flex>
      <v-flex
        xs3
        sm3
        md3
      >
        <v-text-field
          v-model="orderNumber"
          label="Número de orden"
          mask="######"
          box
        ></v-text-field>
      </v-flex>
    </v-layout>
    <v-layout
      row
      wrap
      justify-center
      align-center>
      <v-flex
        xs8
        sm8
        md8
      >
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
      <v-flex
        xs2
        sm2
        md2
      >
        <v-text-field
          v-model="quantity"
          label="Cantidad"
          mask="###"
          box
        ></v-text-field>
      </v-flex>
      <v-flex
        align-center
        xs2
        sm2
        md2
      >
        <v-btn block color="deep-orange" dark @click="addProductToOrder">
          Agregar
        </v-btn>
      </v-flex>
    </v-layout>
    <v-layout row wrap align-center justify-center>
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
    </v-layout>
    <v-layout row wrap justify-center align-center>
      <v-flex>
        <v-btn block color="primary" dark @click="registerOrder">
          Registrar orden
        </v-btn>
      </v-flex>
    </v-layout>
  </div>
</template>

<script>
import Logo from '~/components/Logo.vue'
import VuetifyLogo from '~/components/VuetifyLogo.vue'

export default {
  components: {
    Logo,
    VuetifyLogo
  },
  data(){
    return {
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
    const products = await $axios.$get('/products');
    const customers = await $axios.$get('/customers');
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
      const order = await this.$axios.$post('/orders', {
        orderNumber,
        customerId,
        orderItem: productsTable.map(row => ({ 
          productId: row.id,
          unitPrice: row.unitPrice,
          quantity: row.quantity
        }))
      });
    }
  }
}
</script>
